package io.github.dehuckakpyt.telegrambot.api.client

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.addSerializer
import com.fasterxml.jackson.module.kotlin.jacksonMapperBuilder
import io.github.dehuckakpyt.telegrambot.api.serializer.ContentInputSerializer
import io.github.dehuckakpyt.telegrambot.api.serializer.StringInputSerializer
import io.github.dehuckakpyt.telegrambot.exception.api.TelegramBotApiException
import io.github.dehuckakpyt.telegrambot.ext.toJson
import io.github.dehuckakpyt.telegrambot.model.telegram.input.ContentInput
import io.github.dehuckakpyt.telegrambot.model.telegram.input.StringInput
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.apache.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.ContentType.Application.Json
import io.ktor.http.URLProtocol.Companion.HTTPS
import io.ktor.serialization.jackson.*

/**
 * Created on 19.04.2024.
 *
 * @author Denis Matytsin
 */
public class TelegramApiClient(
    private val token: String,
    clientConfiguration: (HttpClientConfig<ApacheEngineConfig>.() -> Unit)? = null,
) {
    val client = HttpClient(Apache) {
        DEFAULT_CLIENT_CONFIGURATION(token)
        clientConfiguration?.invoke(this)
    }

    private val hiddenToken: String = buildString {
        if (token.length < 12 || token.contains(':').not()) {
            append(token)
            return@buildString
        }

        val visibleFirstCharsCount = 4
        val visibleLastCharsCount = 8

        val hideAfterFirstCharsCount = token.substringBefore(":").length - visibleFirstCharsCount
        val hideBeforeLastCharsCount = token.substringAfter(":").length - visibleLastCharsCount

        append(token.substring(0, visibleFirstCharsCount))
        append("*".repeat(hideAfterFirstCharsCount))
        append(":")
        append("*".repeat(hideBeforeLastCharsCount))
        append(token.substring(token.length - visibleLastCharsCount))
    }

    suspend inline fun <reified R : Any> get(method: String): R = handleRequest(client.get(method))

    suspend inline fun <reified R> get(method: String, block: HttpRequestBuilder.() -> Unit): R =
        handleRequest(client.get(method, block))

    suspend inline fun <reified R : Any> postMultiPart(method: String, noinline block: FormBuilder.() -> Unit): R =
        handleRequest(client.post(method) {
            setBody(MultiPartFormDataContent(formData(block)))
        })

    suspend inline fun <reified R : Any> postJson(method: String, body: Any): R {
        val response = client.post(method) {
            contentType(Json)
            setBody(body)
        }
        val telegramResponse = response.body<TelegramResponse<R>>()

        if (!telegramResponse.ok) throwException(response, telegramResponse, body)

        return telegramResponse.result!!
    }

    suspend inline fun <reified R : Any> handleRequest(response: HttpResponse): R {
        val telegramResponse = response.body<TelegramResponse<R>>()

        if (!telegramResponse.ok) throwException(response, telegramResponse)

        return telegramResponse.result!!
    }

    fun throwException(response: HttpResponse, telegramResponse: TelegramResponse<*>, body: Any? = null) {
        throw TelegramBotApiException(
            """
        Failed request to telegram:
        Request info
        Method: ${response.request.method.value}
        Content-type: ${response.request.contentType()?.contentType}
        Url: ${response.request.url.toString().replace(token, hiddenToken)}
        Body: ${toJson(body)}
        
        Response info
        Code: ${telegramResponse.errorCode}
        Description: ${telegramResponse.description}"""
        )
    }

    suspend fun getFileApi(path: String): HttpResponse {
        return client.get("https://api.telegram.org/file/bot$token/${path}")
    }

    fun toJson(any: Any): String = DEFAULT_MAPPER.writeValueAsString(any)

    fun close(): Unit = client.close()

    companion object {
        private val DEFAULT_MAPPER = jacksonMapperBuilder().apply {
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        }.build().apply {
            setSerializationInclusion(JsonInclude.Include.NON_NULL)

            registerModule(SimpleModule().apply {
                addSerializer(StringInput::class, StringInputSerializer())
                addSerializer(ContentInput::class, ContentInputSerializer())
            })

            registerModule(
                KotlinModule.Builder()
                    .withReflectionCacheSize(512)
                    .configure(KotlinFeature.NullToEmptyCollection, false)
                    .configure(KotlinFeature.NullToEmptyMap, false)
                    .configure(KotlinFeature.NullIsSameAsDefault, false)
                    .configure(KotlinFeature.SingletonSupport, false)
                    .configure(KotlinFeature.StrictNullChecks, false)
                    .build()
            )
        }

        private val DEFAULT_CLIENT_CONFIGURATION: HttpClientConfig<ApacheEngineConfig>.(String) -> Unit = { token ->
            install(ContentNegotiation) {
                register(Json, JacksonConverter(DEFAULT_MAPPER))
            }
            engine {
                socketTimeout = 600_000
            }
            defaultRequest {
                url {
                    protocol = HTTPS
                    host = "api.telegram.org"
                    path("/bot$token/")
                }
            }
        }
    }

    data class TelegramResponse<T>(
        @param:JsonProperty("ok") val ok: Boolean,
        @param:JsonProperty("result") val result: T?,
        @param:JsonProperty("error_code") val errorCode: Int?,
        @param:JsonProperty("description") val description: String?,
    )
}