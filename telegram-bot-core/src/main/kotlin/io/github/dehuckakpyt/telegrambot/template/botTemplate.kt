package io.github.dehuckakpyt.telegrambot.template

import freemarker.template.Configuration
import freemarker.template.Template
import io.github.dehuckakpyt.telegrambot.ext.toKebabCase
import io.ktor.server.config.*
import org.koin.core.qualifier.named
import org.koin.mp.KoinPlatformTools
import java.io.StringWriter
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


class BotTemplate

private val telegramBotTemplate =
    KoinPlatformTools.defaultContext().get().get<ApplicationConfig>(named("telegramBotTemplate"))
//private val templateConfiguration =
//    KoinPlatformTools.defaultContext().get().get<Configuration>(named("templateConfiguration"))

fun template(): ReadOnlyProperty<Any, String> = BotTemplateProperty()

fun template(name: String): Lazy<String> = lazy { getTemplate(name) }

fun template(name: String, defaultTemplate: String): Lazy<String> = lazy {
    getTemplateOrNull(name) ?: defaultTemplate
}

internal class BotTemplateProperty : ReadOnlyProperty<Any, String> {

    private var template: String? = null

    override operator fun getValue(thisRef: Any, property: KProperty<*>): String {
        return template ?: let {
            template = getTemplate(property.name.toKebabCase())
            template!!
        }
    }
}

private fun getTemplate(templateName: String): String {
    return getTemplateOrNull(templateName)
        ?: throw RuntimeException("Не найден шаблон '$templateName'")
}

private fun getTemplateOrNull(templateName: String): String? = telegramBotTemplate.tryGetString(templateName)

//infix fun String.with(instance: Any): String {
//    val writer = StringWriter()
//
//    try {
//        val markerTemplate = Template("template", this, templateConfiguration)
//        markerTemplate.process(instance, writer)
//    } catch (exc: Exception) {
//        throw RuntimeException(exc)
//    }
//
//    return writer.toString()
//}