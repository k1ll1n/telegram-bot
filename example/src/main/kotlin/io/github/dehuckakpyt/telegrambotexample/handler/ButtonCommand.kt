package io.github.dehuckakpyt.telegrambotexample.handler

import io.github.dehuckakpyt.telegrambot.BotHandling
import io.github.dehuckakpyt.telegrambot.ext.callbackButton
import io.github.dehuckakpyt.telegrambot.ext.inlineKeyboard


/**
 * Created on 04.10.2023.
 *<p>
 *
 * @author Denis Matytsin
 */
fun BotHandling.buttonCommand() {
    data class TransferringTest(
        val key: String,
        val value: Int
    )

    val longString = "loooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooong"

    command("/buttons") {
        sendMessage(
            "text", replyMarkup = inlineKeyboard(
                callbackButton("only next", "empty callback"),
                callbackButton("with short callback", "short callback", TransferringTest("1", 1)),
                callbackButton("with long callback", "long callback", TransferringTest(longString, 1))
            )
        )
    }
    callback("empty callback") {
        sendMessage("callback is ${transferredOrNull<TransferringTest>()}")
    }
    callback("short callback") {
        sendMessage("callback is ${transferred<TransferringTest>()}")
    }
    callback("long callback") {
        sendMessage("callback is ${transferred<TransferringTest>()}")
    }
}