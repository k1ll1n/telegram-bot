package io.github.dehuckakpyt.telegrambot.resolver

import io.github.dehuckakpyt.telegrambot.container.CallbackContainer
import io.github.dehuckakpyt.telegrambot.container.Container
import io.github.dehuckakpyt.telegrambot.container.message.CommandContainer
import io.github.dehuckakpyt.telegrambot.container.message.factory.MessageContainerFactory
import io.github.dehuckakpyt.telegrambot.context.container.ContainerCoroutineContext
import io.github.dehuckakpyt.telegrambot.converter.CallbackSerializer
import io.github.dehuckakpyt.telegrambot.exception.handler.ExceptionHandler
import io.github.dehuckakpyt.telegrambot.exception.handler.chain.ChainExceptionHandler
import io.github.dehuckakpyt.telegrambot.ext.update.message.chatId
import io.github.dehuckakpyt.telegrambot.ext.update.message.fetchCommand
import io.github.dehuckakpyt.telegrambot.model.telegram.CallbackQuery
import io.github.dehuckakpyt.telegrambot.model.telegram.Message
import io.github.dehuckakpyt.telegrambot.source.chain.ChainSource
import io.github.dehuckakpyt.telegrambot.source.message.MessageSource
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory


/**
 * Created on 12.11.2023.
 *<p>
 *
 * @author Denis Matytsin
 */
internal class DialogUpdateResolver(
    private val callbackSerializer: CallbackSerializer,
    private val chainSource: ChainSource,
    private val chainResolver: ChainResolver,
    private val exceptionHandler: ExceptionHandler,
    private val chainExceptionHandler: ChainExceptionHandler,
    private val messageArgumentFactories: List<MessageContainerFactory>,
    private val messageSource: MessageSource,
    private val username: String,
) {

    private val logger = LoggerFactory.getLogger(DialogUpdateResolver::class.java)

    suspend fun processMessage(message: Message): Unit {
        val from = message.from
        val text = message.text
        val chat = message.chat

        if (from == null) {
            logger.warn("Don't expect message without fromId.\nchatId = '${chat.id}'\ntext = $text")
            return
        }

        val command = fetchCommand(text, username)
        if (command != null)
            exceptionHandler.executeCommand(message) {
                processCommand(command, message)
            }
        else
            exceptionHandler.executeStep(message) {
                processStep(message)
            }
    }

    private suspend fun processCommand(command: String, message: Message): Unit = with(message) {
        messageSource.save(
            message = message,
            fromBot = false,
            type = "COMMAND",
            step = command,
            stepContainerType = "COMMAND",
            text = text
        )

        chainResolver.getCommand(command).let { action ->
            invokeWithContainerContext(CommandContainer(message, step = command), action)
        }
    }

    private suspend fun processStep(message: Message): Unit = with(message) {
        val chain = chainSource.get(chatId, from!!.id)
        val factory = message.messageContainerFactory

        messageSource.save(
            message = message,
            fromBot = false,
            type = factory.messageType,
            step = chain?.step,
            stepContainerType = factory.messageType,
            text = factory.getMessageText(message),
            fileIds = factory.getMessageFileIds(message)
        )

        val action = chain?.step?.let { chainResolver.getStep(it, factory.type) }

        if (action == null) {
            if (chat.type == "private") chainExceptionHandler.whenStepNotFound() else return
        }

        invokeWithContainerContext(factory.create(message, chain.step, chain.content), action)
    }

    suspend fun processCallback(callback: CallbackQuery): Unit = with(callback) {
        val data = data ?: return
        if (message !is Message) return

        exceptionHandler.executeCallback(callback) {
            val (callbackName, callbackContent) = callbackSerializer.fromCallback(data)

            chainResolver.getCallback(callbackName)?.let { action ->
                invokeWithContainerContext(CallbackContainer(callback, step = callbackName, callbackContent), action)
            }
        }
    }

    internal val allowedUpdates: Set<String> get() = chainResolver.allowedUpdates

    private val Message.messageContainerFactory: MessageContainerFactory
        get() = messageArgumentFactories.firstOrNull { it.matches(this) }
            ?: throw RuntimeException("Not found factory for received argument type.")


    private suspend fun <T : Container> invokeWithContainerContext(container: T, action: suspend T.() -> Unit) =
        withContext(ContainerCoroutineContext(container)) {
            action.invoke(container)
        }
}