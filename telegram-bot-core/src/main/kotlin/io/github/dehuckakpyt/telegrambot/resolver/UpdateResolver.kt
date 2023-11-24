package io.github.dehuckakpyt.telegrambot.resolver

import com.dehucka.microservice.logger.Logging
import com.elbekd.bot.types.CallbackQuery
import com.elbekd.bot.types.Message
import com.elbekd.bot.types.Update
import com.elbekd.bot.types.UpdateMessage
import io.github.dehuckakpyt.telegrambot.TelegramBot
import io.github.dehuckakpyt.telegrambot.container.*
import io.github.dehuckakpyt.telegrambot.container.factory.MessageContainerFactory
import io.github.dehuckakpyt.telegrambot.context.InternalKoinComponent
import io.github.dehuckakpyt.telegrambot.context.getInternal
import io.github.dehuckakpyt.telegrambot.converter.CallbackSerializer
import io.github.dehuckakpyt.telegrambot.converter.ContentConverter
import io.github.dehuckakpyt.telegrambot.exception.chat.ChatException
import io.github.dehuckakpyt.telegrambot.exception.handler.ExceptionHandler
import io.github.dehuckakpyt.telegrambot.ext.chatId
import io.github.dehuckakpyt.telegrambot.source.chain.ChainSource
import io.github.dehuckakpyt.telegrambot.source.message.MessageSource
import io.github.dehuckakpyt.telegrambot.template.Templating
import org.koin.core.component.get


/**
 * Created on 12.11.2023.
 *<p>
 *
 * @author Denis Matytsin
 */
internal class UpdateResolver : InternalKoinComponent, Templating, Logging {

    private val bot = get<TelegramBot>()
    private val callbackSerializer = get<CallbackSerializer>()
    private val chainSource = get<ChainSource>()
    private val messageSource = get<MessageSource>()
    private val chainResolver = getInternal<ChainResolver>()
    private val contentConverter = getInternal<ContentConverter>()
    private val exceptionHandler = getInternal<ExceptionHandler>()

    suspend fun processUpdate(update: Update): Unit {
        if (update !is UpdateMessage) return

        val message = update.message
        val from = message.from
        val text = message.text
        val chatId = message.chatId

        if (from == null) {
            logger.warn("Don't expect message without fromId.\nchatId = '$chatId'\ntext = $text")
            return
        }

        messageSource.save(chatId, from.id, message.messageId, text)

        exceptionHandler.execute(chatId) {
            CommandMessageContainer.fetchCommand(text)?.let {
                processCommand(it, message)
            } ?: processMessage(message)
        }
    }

    private suspend fun processCommand(command: String, message: Message): Unit = with(message) {
        chainResolver.getCommand(command)
            .invoke(CommandMessageContainer(chatId, message, chainSource, contentConverter, bot))
    }

    private suspend fun processMessage(message: Message): Unit = with(message) {
        val chain = chainSource.get(chatId, from!!.id)
        val factory = message.containerFactory
        val action = chainResolver.getStep(chain?.step, factory.type)

        action.invoke(factory.create(chatId, message, chain!!.content, chainSource, contentConverter, bot))
    }

    suspend fun processCallback(callback: CallbackQuery): Unit = with(callback) {
        val data = data ?: return

        exceptionHandler.execute(chatId) {
            val (callbackName, callbackContent) = callbackSerializer.fromCallback(data)

            chainResolver.getCallback(callbackName)?.invoke(
                CallbackMessageContainer(chatId, callback, callbackContent, chainSource, contentConverter, bot)
            )
        }
    }

    private val Message.containerFactory: MessageContainerFactory
        get() = when {
            TextMessageContainer.matches(this) -> TextMessageContainer.Companion
            AudioMessageContainer.matches(this) -> AudioMessageContainer.Companion
            VoiceMessageContainer.matches(this) -> VoiceMessageContainer.Companion
            ContactMessageContainer.matches(this) -> ContactMessageContainer.Companion
            DocumentMessageContainer.matches(this) -> DocumentMessageContainer.Companion
            PhotoMessageContainer.matches(this) -> PhotoMessageContainer.Companion
            else -> throw ChatException("Такой тип сообщения ещё не поддерживается.")
        }
}