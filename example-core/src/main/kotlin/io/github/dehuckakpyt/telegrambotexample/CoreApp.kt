package io.github.dehuckakpyt.telegrambotexample

import io.github.dehuckakpyt.telegrambot.config.TelegramBotConfig
import io.github.dehuckakpyt.telegrambot.ext.config.receiver.handling
import io.github.dehuckakpyt.telegrambot.ext.config.receiver.longPolling
import io.github.dehuckakpyt.telegrambot.ext.config.receiver.updateHandling
import io.github.dehuckakpyt.telegrambot.factory.TelegramBotFactory
import io.github.dehuckakpyt.telegrambotexample.exception.CustomExceptionHandler
import io.github.dehuckakpyt.telegrambotexample.handling.exceptionCommand
import io.github.dehuckakpyt.telegrambotexample.handling.startCommand
import io.github.dehuckakpyt.telegrambotexample.handling.update.onSomeEvent

/**
 * Created on 13.08.2023.
 *<p>
 *
 * @author Denis Matytsin
 */
suspend fun main(args: Array<String>): Unit {
    val config = TelegramBotConfig().apply {
        token = "<>"
        username = "<>"

        receiving {
            longPolling {
                limit = 10
                timeout = 25
            }

            exceptionHandler = { CustomExceptionHandler(telegramBot, receiving.messageTemplate, templater) }

            handling {
                startCommand()
                exceptionCommand()
            }

            updateHandling {
                onSomeEvent()
            }
        }
    }

    val context = TelegramBotFactory.createTelegramBotContext(config)
    val bot = context.telegramBot
    val updateReceiver = context.updateReceiver
    val templater = context.templater

    updateReceiver.start()
    bot.sendMessage(1165327523L, "Telegram Bot ${bot.username} started!")

    readlnOrNull()
    updateReceiver.stop()
}
