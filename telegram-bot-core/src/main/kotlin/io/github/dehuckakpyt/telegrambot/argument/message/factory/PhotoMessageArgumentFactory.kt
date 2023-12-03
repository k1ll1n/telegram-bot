package io.github.dehuckakpyt.telegrambot.argument.message.factory

import com.elbekd.bot.types.Message
import io.github.dehuckakpyt.telegrambot.argument.message.MessageArgument
import io.github.dehuckakpyt.telegrambot.argument.message.MessageType
import io.github.dehuckakpyt.telegrambot.argument.message.PhotoMessageArgument
import kotlin.reflect.KClass


/**
 * Created on 03.12.2023.
 *<p>
 *
 * @author Denis Matytsin
 */
internal class PhotoMessageArgumentFactory : MessageArgumentFactory {

    override fun matches(message: Message): Boolean = with(message) {
        return photo.isNotEmpty()
    }

    override fun create(chatId: Long, message: Message, content: String?): MessageArgument =
        PhotoMessageArgument(chatId, message, content)

    override val type: KClass<out MessageArgument> = MessageType.PHOTO
}