package io.github.dehuckakpyt.telegrambot.container

import com.dehucka.microservice.ext.shortMapper
import com.elbekd.bot.types.User
import com.fasterxml.jackson.module.kotlin.readValue
import io.github.dehuckakpyt.telegrambot.TelegramBot
import io.github.dehuckakpyt.telegrambot.source.chain.ChainSource


/**
 * Created on 13.08.2023.
 *<p>
 *
 * @author Denis Matytsin
 */
abstract class Container(
    chatId: Long,
    val content: String?,
    private val chainSource: ChainSource,
    bot: TelegramBot,
) : TelegramApiContainer(chatId, bot) {

    abstract val from: User?

    private var nextStep: String? = null
    private var nextStepInstance: Any? = null

    fun next(step: String?) {
        nextStep = step
    }

    fun next(step: String, instance: Any) {
        nextStep = step
        nextStepInstance = instance
    }

    fun transferToNext(instance: Any) {
        nextStepInstance = instance
    }

    inline fun <reified T> transferred(): T {
        return content?.let { shortMapper.readValue(it) }
            ?: throw RuntimeException("Ожидается экземпляр класса ${T::class.simpleName}, но в chainSource.content ничего не сохранено.")
    }

    inline fun <reified T> transferredOrNull(): T? {
        return content?.let { shortMapper.readValue(it) }
    }

    internal suspend fun finalize() {
        chainSource.save(chatId, from?.id, nextStep, nextStepInstance.toContent())
    }

    private fun Any?.toContent(): String? {
        return this?.let { shortMapper.writeValueAsString(it) }
    }
}