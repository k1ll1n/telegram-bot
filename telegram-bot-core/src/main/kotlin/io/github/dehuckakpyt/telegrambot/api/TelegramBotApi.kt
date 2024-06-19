package io.github.dehuckakpyt.telegrambot.api

import io.github.dehuckakpyt.telegrambot.model.telegram.BotCommand
import io.github.dehuckakpyt.telegrambot.model.telegram.BotCommandScope
import io.github.dehuckakpyt.telegrambot.model.telegram.BotDescription
import io.github.dehuckakpyt.telegrambot.model.telegram.BotName
import io.github.dehuckakpyt.telegrambot.model.telegram.BotShortDescription
import io.github.dehuckakpyt.telegrambot.model.telegram.BusinessConnection
import io.github.dehuckakpyt.telegrambot.model.telegram.ChatAdministratorRights
import io.github.dehuckakpyt.telegrambot.model.telegram.ChatFullInfo
import io.github.dehuckakpyt.telegrambot.model.telegram.ChatInviteLink
import io.github.dehuckakpyt.telegrambot.model.telegram.ChatMember
import io.github.dehuckakpyt.telegrambot.model.telegram.ChatPermissions
import io.github.dehuckakpyt.telegrambot.model.telegram.File
import io.github.dehuckakpyt.telegrambot.model.telegram.ForumTopic
import io.github.dehuckakpyt.telegrambot.model.telegram.GameHighScore
import io.github.dehuckakpyt.telegrambot.model.telegram.InlineKeyboardMarkup
import io.github.dehuckakpyt.telegrambot.model.telegram.InlineQueryResult
import io.github.dehuckakpyt.telegrambot.model.telegram.InlineQueryResultsButton
import io.github.dehuckakpyt.telegrambot.model.telegram.InputMedia
import io.github.dehuckakpyt.telegrambot.model.telegram.InputPollOption
import io.github.dehuckakpyt.telegrambot.model.telegram.InputSticker
import io.github.dehuckakpyt.telegrambot.model.telegram.LabeledPrice
import io.github.dehuckakpyt.telegrambot.model.telegram.LinkPreviewOptions
import io.github.dehuckakpyt.telegrambot.model.telegram.MaskPosition
import io.github.dehuckakpyt.telegrambot.model.telegram.MenuButton
import io.github.dehuckakpyt.telegrambot.model.telegram.Message
import io.github.dehuckakpyt.telegrambot.model.telegram.MessageEntity
import io.github.dehuckakpyt.telegrambot.model.telegram.MessageId
import io.github.dehuckakpyt.telegrambot.model.telegram.PassportElementError
import io.github.dehuckakpyt.telegrambot.model.telegram.Poll
import io.github.dehuckakpyt.telegrambot.model.telegram.ReactionType
import io.github.dehuckakpyt.telegrambot.model.telegram.ReplyMarkup
import io.github.dehuckakpyt.telegrambot.model.telegram.ReplyParameters
import io.github.dehuckakpyt.telegrambot.model.telegram.SentWebAppMessage
import io.github.dehuckakpyt.telegrambot.model.telegram.ShippingOption
import io.github.dehuckakpyt.telegrambot.model.telegram.StarTransactions
import io.github.dehuckakpyt.telegrambot.model.telegram.Sticker
import io.github.dehuckakpyt.telegrambot.model.telegram.StickerSet
import io.github.dehuckakpyt.telegrambot.model.telegram.Update
import io.github.dehuckakpyt.telegrambot.model.telegram.User
import io.github.dehuckakpyt.telegrambot.model.telegram.UserChatBoosts
import io.github.dehuckakpyt.telegrambot.model.telegram.UserProfilePhotos
import io.github.dehuckakpyt.telegrambot.model.telegram.WebhookInfo
import io.github.dehuckakpyt.telegrambot.model.telegram.input.ContentInput
import io.github.dehuckakpyt.telegrambot.model.telegram.input.Input
import kotlin.Boolean
import kotlin.Double
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.collections.Iterable
import kotlin.collections.List

/**
 * @author KScript
 */
public interface TelegramBotApi {
    /**
     * Use this method to receive incoming updates using long polling
     * ([wiki](https://en.wikipedia.org/wiki/Push_technology#Long_polling)). Returns an Array of
     * [Update](https://core.telegram.org/bots/api/#update) objects.
     *
     * @param offset Identifier of the first update to be returned. Must be greater by one than the
     * highest among the identifiers of previously received updates. By default, updates starting with
     * the earliest unconfirmed update are returned. An update is considered confirmed as soon as
     * [getUpdates](https://core.telegram.org/bots/api/#getupdates) is called with an *offset* higher
     * than its *update_id*. The negative offset can be specified to retrieve updates starting from
     * *-offset* update from the end of the updates queue. All previous updates will be forgotten.
     * @param limit Limits the number of updates to be retrieved. Values between 1-100 are accepted.
     * Defaults to 100.
     * @param timeout Timeout in seconds for long polling. Defaults to 0, i.e. usual short polling.
     * Should be positive, short polling should be used for testing purposes only.
     * @param allowedUpdates A JSON-serialized list of the update types you want your bot to
     * receive. For example, specify `["message", "edited_channel_post", "callback_query"]` to only
     * receive updates of these types. See [Update](https://core.telegram.org/bots/api/#update) for a
     * complete list of available update types. Specify an empty list to receive all update types
     * except *chat_member*, *message_reaction*, and *message_reaction_count* (default). If not
     * specified, the previous setting will be used.  
     *
     * Please note that this parameter doesn't affect updates created before the call to the
     * getUpdates, so unwanted updates may be received for a short period of time.
     */
    public suspend fun getUpdates(
        offset: Long? = null,
        limit: Int? = null,
        timeout: Int? = null,
        allowedUpdates: Iterable<String>? = null,
    ): List<Update>

    /**
     * Use this method to specify a URL and receive incoming updates via an outgoing webhook.
     * Whenever there is an update for the bot, we will send an HTTPS POST request to the specified
     * URL, containing a JSON-serialized [Update](https://core.telegram.org/bots/api/#update). In case
     * of an unsuccessful request, we will give up after a reasonable amount of attempts. Returns
     * *True* on success.
     *
     * If you'd like to make sure that the webhook was set by you, you can specify secret data in
     * the parameter *secret_token*. If specified, the request will contain a header
     * “X-Telegram-Bot-Api-Secret-Token” with the secret token as content.
     *
     * @param url HTTPS URL to send updates to. Use an empty string to remove webhook integration
     * @param certificate Upload your public key certificate so that the root certificate in use can
     * be checked. See our [self-signed guide](https://core.telegram.org/bots/self-signed) for details.
     * @param ipAddress The fixed IP address which will be used to send webhook requests instead of
     * the IP address resolved through DNS
     * @param maxConnections The maximum allowed number of simultaneous HTTPS connections to the
     * webhook for update delivery, 1-100. Defaults to *40*. Use lower values to limit the load on your
     * bot's server, and higher values to increase your bot's throughput.
     * @param allowedUpdates A JSON-serialized list of the update types you want your bot to
     * receive. For example, specify `["message", "edited_channel_post", "callback_query"]` to only
     * receive updates of these types. See [Update](https://core.telegram.org/bots/api/#update) for a
     * complete list of available update types. Specify an empty list to receive all update types
     * except *chat_member*, *message_reaction*, and *message_reaction_count* (default). If not
     * specified, the previous setting will be used.  
     * Please note that this parameter doesn't affect updates created before the call to the
     * setWebhook, so unwanted updates may be received for a short period of time.
     * @param dropPendingUpdates Pass *True* to drop all pending updates
     * @param secretToken A secret token to be sent in a header “X-Telegram-Bot-Api-Secret-Token” in
     * every webhook request, 1-256 characters. Only characters `A-Z`, `a-z`, `0-9`, `_` and `-` are
     * allowed. The header is useful to ensure that the request comes from a webhook set by you.
     */
    public suspend fun setWebhook(
        url: String,
        certificate: ContentInput? = null,
        ipAddress: String? = null,
        maxConnections: Int? = null,
        allowedUpdates: Iterable<String>? = null,
        dropPendingUpdates: Boolean? = null,
        secretToken: String? = null,
    ): Boolean

    /**
     * Use this method to remove webhook integration if you decide to switch back to
     * [getUpdates](https://core.telegram.org/bots/api/#getupdates). Returns *True* on success.
     *
     * @param dropPendingUpdates Pass *True* to drop all pending updates
     */
    public suspend fun deleteWebhook(dropPendingUpdates: Boolean? = null): Boolean

    /**
     * Use this method to get current webhook status. Requires no parameters. On success, returns a
     * [WebhookInfo](https://core.telegram.org/bots/api/#webhookinfo) object. If the bot is using
     * [getUpdates](https://core.telegram.org/bots/api/#getupdates), will return an object with the
     * *url* field empty.
     */
    public suspend fun getWebhookInfo(): WebhookInfo

    /**
     * A simple method for testing your bot's authentication token. Requires no parameters. Returns
     * basic information about the bot in form of a [User](https://core.telegram.org/bots/api/#user)
     * object.
     */
    public suspend fun getMe(): User

    /**
     * Use this method to log out from the cloud Bot API server before launching the bot locally.
     * You **must** log out the bot before running it locally, otherwise there is no guarantee that the
     * bot will receive updates. After a successful call, you can immediately log in on a local server,
     * but will not be able to log in back to the cloud Bot API server for 10 minutes. Returns *True*
     * on success. Requires no parameters.
     */
    public suspend fun logOut(): Boolean

    /**
     * Use this method to close the bot instance before moving it from one local server to another.
     * You need to delete the webhook before calling this method to ensure that the bot isn't launched
     * again after server restart. The method will return error 429 in the first 10 minutes after the
     * bot is launched. Returns *True* on success. Requires no parameters.
     */
    public suspend fun close(): Boolean

    /**
     * Use this method to send text messages. On success, the sent
     * [Message](https://core.telegram.org/bots/api/#message) is returned.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the
     * format `@channelusername`)
     * @param text Text of the message to be sent, 1-4096 characters after entities parsing
     * @param businessConnectionId Unique identifier of the business connection on behalf of which
     * the message will be sent
     * @param messageThreadId Unique identifier for the target message thread (topic) of the forum;
     * for forum supergroups only
     * @param parseMode Mode for parsing entities in the message text. See [formatting
     * options](https://core.telegram.org/bots/api/#formatting-options) for more details.
     * @param entities A JSON-serialized list of special entities that appear in message text, which
     * can be specified instead of *parse_mode*
     * @param linkPreviewOptions Link preview generation options for the message
     * @param disableNotification Sends the message
     * [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a
     * notification with no sound.
     * @param protectContent Protects the contents of the sent message from forwarding and saving
     * @param messageEffectId Unique identifier of the message effect to be added to the message;
     * for private chats only
     * @param replyParameters Description of the message to reply to
     * @param replyMarkup Additional interface options. A JSON-serialized object for an [inline
     * keyboard](https://core.telegram.org/bots/features#inline-keyboards), [custom reply
     * keyboard](https://core.telegram.org/bots/features#keyboards), instructions to remove a reply
     * keyboard or to force a reply from the user
     */
    public suspend fun sendMessage(
        chatId: String,
        text: String,
        businessConnectionId: String? = null,
        messageThreadId: Long? = null,
        parseMode: String? = null,
        entities: Iterable<MessageEntity>? = null,
        linkPreviewOptions: LinkPreviewOptions? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        messageEffectId: String? = null,
        replyParameters: ReplyParameters? = null,
        replyMarkup: ReplyMarkup? = null,
    ): Message

    /**
     * Use this method to forward messages of any kind. Service messages and messages with protected
     * content can't be forwarded. On success, the sent
     * [Message](https://core.telegram.org/bots/api/#message) is returned.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the
     * format `@channelusername`)
     * @param fromChatId Unique identifier for the chat where the original message was sent (or
     * channel username in the format `@channelusername`)
     * @param messageId Message identifier in the chat specified in *from_chat_id*
     * @param messageThreadId Unique identifier for the target message thread (topic) of the forum;
     * for forum supergroups only
     * @param disableNotification Sends the message
     * [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a
     * notification with no sound.
     * @param protectContent Protects the contents of the forwarded message from forwarding and
     * saving
     */
    public suspend fun forwardMessage(
        chatId: String,
        fromChatId: String,
        messageId: Long,
        messageThreadId: Long? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
    ): Message

    /**
     * Use this method to forward multiple messages of any kind. If some of the specified messages
     * can't be found or forwarded, they are skipped. Service messages and messages with protected
     * content can't be forwarded. Album grouping is kept for forwarded messages. On success, an array
     * of [MessageId](https://core.telegram.org/bots/api/#messageid) of the sent messages is returned.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the
     * format `@channelusername`)
     * @param fromChatId Unique identifier for the chat where the original messages were sent (or
     * channel username in the format `@channelusername`)
     * @param messageIds A JSON-serialized list of 1-100 identifiers of messages in the chat
     * *from_chat_id* to forward. The identifiers must be specified in a strictly increasing order.
     * @param messageThreadId Unique identifier for the target message thread (topic) of the forum;
     * for forum supergroups only
     * @param disableNotification Sends the messages
     * [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a
     * notification with no sound.
     * @param protectContent Protects the contents of the forwarded messages from forwarding and
     * saving
     */
    public suspend fun forwardMessages(
        chatId: String,
        fromChatId: String,
        messageIds: Iterable<Long>,
        messageThreadId: Long? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
    ): List<MessageId>

    /**
     * Use this method to copy messages of any kind. Service messages, giveaway messages, giveaway
     * winners messages, and invoice messages can't be copied. A quiz
     * [poll](https://core.telegram.org/bots/api/#poll) can be copied only if the value of the field
     * *correct_option_id* is known to the bot. The method is analogous to the method
     * [forwardMessage](https://core.telegram.org/bots/api/#forwardmessage), but the copied message
     * doesn't have a link to the original message. Returns the
     * [MessageId](https://core.telegram.org/bots/api/#messageid) of the sent message on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the
     * format `@channelusername`)
     * @param fromChatId Unique identifier for the chat where the original message was sent (or
     * channel username in the format `@channelusername`)
     * @param messageId Message identifier in the chat specified in *from_chat_id*
     * @param messageThreadId Unique identifier for the target message thread (topic) of the forum;
     * for forum supergroups only
     * @param caption New caption for media, 0-1024 characters after entities parsing. If not
     * specified, the original caption is kept
     * @param parseMode Mode for parsing entities in the new caption. See [formatting
     * options](https://core.telegram.org/bots/api/#formatting-options) for more details.
     * @param captionEntities A JSON-serialized list of special entities that appear in the new
     * caption, which can be specified instead of *parse_mode*
     * @param showCaptionAboveMedia Pass *True*, if the caption must be shown above the message
     * media. Ignored if a new caption isn't specified.
     * @param disableNotification Sends the message
     * [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a
     * notification with no sound.
     * @param protectContent Protects the contents of the sent message from forwarding and saving
     * @param replyParameters Description of the message to reply to
     * @param replyMarkup Additional interface options. A JSON-serialized object for an [inline
     * keyboard](https://core.telegram.org/bots/features#inline-keyboards), [custom reply
     * keyboard](https://core.telegram.org/bots/features#keyboards), instructions to remove a reply
     * keyboard or to force a reply from the user
     */
    public suspend fun copyMessage(
        chatId: String,
        fromChatId: String,
        messageId: Long,
        messageThreadId: Long? = null,
        caption: String? = null,
        parseMode: String? = null,
        captionEntities: Iterable<MessageEntity>? = null,
        showCaptionAboveMedia: Boolean? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        replyParameters: ReplyParameters? = null,
        replyMarkup: ReplyMarkup? = null,
    ): MessageId

    /**
     * Use this method to copy messages of any kind. If some of the specified messages can't be
     * found or copied, they are skipped. Service messages, giveaway messages, giveaway winners
     * messages, and invoice messages can't be copied. A quiz
     * [poll](https://core.telegram.org/bots/api/#poll) can be copied only if the value of the field
     * *correct_option_id* is known to the bot. The method is analogous to the method
     * [forwardMessages](https://core.telegram.org/bots/api/#forwardmessages), but the copied messages
     * don't have a link to the original message. Album grouping is kept for copied messages. On
     * success, an array of [MessageId](https://core.telegram.org/bots/api/#messageid) of the sent
     * messages is returned.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the
     * format `@channelusername`)
     * @param fromChatId Unique identifier for the chat where the original messages were sent (or
     * channel username in the format `@channelusername`)
     * @param messageIds A JSON-serialized list of 1-100 identifiers of messages in the chat
     * *from_chat_id* to copy. The identifiers must be specified in a strictly increasing order.
     * @param messageThreadId Unique identifier for the target message thread (topic) of the forum;
     * for forum supergroups only
     * @param disableNotification Sends the messages
     * [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a
     * notification with no sound.
     * @param protectContent Protects the contents of the sent messages from forwarding and saving
     * @param removeCaption Pass *True* to copy the messages without their captions
     */
    public suspend fun copyMessages(
        chatId: String,
        fromChatId: String,
        messageIds: Iterable<Long>,
        messageThreadId: Long? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        removeCaption: Boolean? = null,
    ): List<MessageId>

    /**
     * Use this method to send photos. On success, the sent
     * [Message](https://core.telegram.org/bots/api/#message) is returned.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the
     * format `@channelusername`)
     * @param photo Photo to send. Pass a file_id as String to send a photo that exists on the
     * Telegram servers (recommended), pass an HTTP URL as a String for Telegram to get a photo from
     * the Internet, or upload a new photo using multipart/form-data. The photo must be at most 10 MB
     * in size. The photo's width and height must not exceed 10000 in total. Width and height ratio
     * must be at most 20. [More information on Sending Files
     * ](https://core.telegram.org/bots/api/#sending-files)
     * @param businessConnectionId Unique identifier of the business connection on behalf of which
     * the message will be sent
     * @param messageThreadId Unique identifier for the target message thread (topic) of the forum;
     * for forum supergroups only
     * @param caption Photo caption (may also be used when resending photos by *file_id*), 0-1024
     * characters after entities parsing
     * @param parseMode Mode for parsing entities in the photo caption. See [formatting
     * options](https://core.telegram.org/bots/api/#formatting-options) for more details.
     * @param captionEntities A JSON-serialized list of special entities that appear in the caption,
     * which can be specified instead of *parse_mode*
     * @param showCaptionAboveMedia Pass *True*, if the caption must be shown above the message
     * media
     * @param hasSpoiler Pass *True* if the photo needs to be covered with a spoiler animation
     * @param disableNotification Sends the message
     * [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a
     * notification with no sound.
     * @param protectContent Protects the contents of the sent message from forwarding and saving
     * @param messageEffectId Unique identifier of the message effect to be added to the message;
     * for private chats only
     * @param replyParameters Description of the message to reply to
     * @param replyMarkup Additional interface options. A JSON-serialized object for an [inline
     * keyboard](https://core.telegram.org/bots/features#inline-keyboards), [custom reply
     * keyboard](https://core.telegram.org/bots/features#keyboards), instructions to remove a reply
     * keyboard or to force a reply from the user
     */
    public suspend fun sendPhoto(
        chatId: String,
        photo: Input,
        businessConnectionId: String? = null,
        messageThreadId: Long? = null,
        caption: String? = null,
        parseMode: String? = null,
        captionEntities: Iterable<MessageEntity>? = null,
        showCaptionAboveMedia: Boolean? = null,
        hasSpoiler: Boolean? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        messageEffectId: String? = null,
        replyParameters: ReplyParameters? = null,
        replyMarkup: ReplyMarkup? = null,
    ): Message

    /**
     * Use this method to send audio files, if you want Telegram clients to display them in the
     * music player. Your audio must be in the .MP3 or .M4A format. On success, the sent
     * [Message](https://core.telegram.org/bots/api/#message) is returned. Bots can currently send
     * audio files of up to 50 MB in size, this limit may be changed in the future.
     *
     * For sending voice messages, use the
     * [sendVoice](https://core.telegram.org/bots/api/#sendvoice) method instead.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the
     * format `@channelusername`)
     * @param audio Audio file to send. Pass a file_id as String to send an audio file that exists
     * on the Telegram servers (recommended), pass an HTTP URL as a String for Telegram to get an audio
     * file from the Internet, or upload a new one using multipart/form-data. [More information on
     * Sending Files ](https://core.telegram.org/bots/api/#sending-files)
     * @param businessConnectionId Unique identifier of the business connection on behalf of which
     * the message will be sent
     * @param messageThreadId Unique identifier for the target message thread (topic) of the forum;
     * for forum supergroups only
     * @param caption Audio caption, 0-1024 characters after entities parsing
     * @param parseMode Mode for parsing entities in the audio caption. See [formatting
     * options](https://core.telegram.org/bots/api/#formatting-options) for more details.
     * @param captionEntities A JSON-serialized list of special entities that appear in the caption,
     * which can be specified instead of *parse_mode*
     * @param duration Duration of the audio in seconds
     * @param performer Performer
     * @param title Track name
     * @param thumbnail Thumbnail of the file sent; can be ignored if thumbnail generation for the
     * file is supported server-side. The thumbnail should be in JPEG format and less than 200 kB in
     * size. A thumbnail's width and height should not exceed 320. Ignored if the file is not uploaded
     * using multipart/form-data. Thumbnails can't be reused and can be only uploaded as a new file, so
     * you can pass “attach://\<file_attach_name\>” if the thumbnail was uploaded using
     * multipart/form-data under \<file_attach_name\>. [More information on Sending Files
     * ](https://core.telegram.org/bots/api/#sending-files)
     * @param disableNotification Sends the message
     * [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a
     * notification with no sound.
     * @param protectContent Protects the contents of the sent message from forwarding and saving
     * @param messageEffectId Unique identifier of the message effect to be added to the message;
     * for private chats only
     * @param replyParameters Description of the message to reply to
     * @param replyMarkup Additional interface options. A JSON-serialized object for an [inline
     * keyboard](https://core.telegram.org/bots/features#inline-keyboards), [custom reply
     * keyboard](https://core.telegram.org/bots/features#keyboards), instructions to remove a reply
     * keyboard or to force a reply from the user
     */
    public suspend fun sendAudio(
        chatId: String,
        audio: Input,
        businessConnectionId: String? = null,
        messageThreadId: Long? = null,
        caption: String? = null,
        parseMode: String? = null,
        captionEntities: Iterable<MessageEntity>? = null,
        duration: Int? = null,
        performer: String? = null,
        title: String? = null,
        thumbnail: ContentInput? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        messageEffectId: String? = null,
        replyParameters: ReplyParameters? = null,
        replyMarkup: ReplyMarkup? = null,
    ): Message

    /**
     * Use this method to send general files. On success, the sent
     * [Message](https://core.telegram.org/bots/api/#message) is returned. Bots can currently send
     * files of any type of up to 50 MB in size, this limit may be changed in the future.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the
     * format `@channelusername`)
     * @param document File to send. Pass a file_id as String to send a file that exists on the
     * Telegram servers (recommended), pass an HTTP URL as a String for Telegram to get a file from the
     * Internet, or upload a new one using multipart/form-data. [More information on Sending Files
     * ](https://core.telegram.org/bots/api/#sending-files)
     * @param businessConnectionId Unique identifier of the business connection on behalf of which
     * the message will be sent
     * @param messageThreadId Unique identifier for the target message thread (topic) of the forum;
     * for forum supergroups only
     * @param thumbnail Thumbnail of the file sent; can be ignored if thumbnail generation for the
     * file is supported server-side. The thumbnail should be in JPEG format and less than 200 kB in
     * size. A thumbnail's width and height should not exceed 320. Ignored if the file is not uploaded
     * using multipart/form-data. Thumbnails can't be reused and can be only uploaded as a new file, so
     * you can pass “attach://\<file_attach_name\>” if the thumbnail was uploaded using
     * multipart/form-data under \<file_attach_name\>. [More information on Sending Files
     * ](https://core.telegram.org/bots/api/#sending-files)
     * @param caption Document caption (may also be used when resending documents by *file_id*),
     * 0-1024 characters after entities parsing
     * @param parseMode Mode for parsing entities in the document caption. See [formatting
     * options](https://core.telegram.org/bots/api/#formatting-options) for more details.
     * @param captionEntities A JSON-serialized list of special entities that appear in the caption,
     * which can be specified instead of *parse_mode*
     * @param disableContentTypeDetection Disables automatic server-side content type detection for
     * files uploaded using multipart/form-data
     * @param disableNotification Sends the message
     * [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a
     * notification with no sound.
     * @param protectContent Protects the contents of the sent message from forwarding and saving
     * @param messageEffectId Unique identifier of the message effect to be added to the message;
     * for private chats only
     * @param replyParameters Description of the message to reply to
     * @param replyMarkup Additional interface options. A JSON-serialized object for an [inline
     * keyboard](https://core.telegram.org/bots/features#inline-keyboards), [custom reply
     * keyboard](https://core.telegram.org/bots/features#keyboards), instructions to remove a reply
     * keyboard or to force a reply from the user
     */
    public suspend fun sendDocument(
        chatId: String,
        document: Input,
        businessConnectionId: String? = null,
        messageThreadId: Long? = null,
        thumbnail: ContentInput? = null,
        caption: String? = null,
        parseMode: String? = null,
        captionEntities: Iterable<MessageEntity>? = null,
        disableContentTypeDetection: Boolean? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        messageEffectId: String? = null,
        replyParameters: ReplyParameters? = null,
        replyMarkup: ReplyMarkup? = null,
    ): Message

    /**
     * Use this method to send video files, Telegram clients support MPEG4 videos (other formats may
     * be sent as [Document](https://core.telegram.org/bots/api/#document)). On success, the sent
     * [Message](https://core.telegram.org/bots/api/#message) is returned. Bots can currently send
     * video files of up to 50 MB in size, this limit may be changed in the future.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the
     * format `@channelusername`)
     * @param video Video to send. Pass a file_id as String to send a video that exists on the
     * Telegram servers (recommended), pass an HTTP URL as a String for Telegram to get a video from
     * the Internet, or upload a new video using multipart/form-data. [More information on Sending
     * Files ](https://core.telegram.org/bots/api/#sending-files)
     * @param businessConnectionId Unique identifier of the business connection on behalf of which
     * the message will be sent
     * @param messageThreadId Unique identifier for the target message thread (topic) of the forum;
     * for forum supergroups only
     * @param duration Duration of sent video in seconds
     * @param width Video width
     * @param height Video height
     * @param thumbnail Thumbnail of the file sent; can be ignored if thumbnail generation for the
     * file is supported server-side. The thumbnail should be in JPEG format and less than 200 kB in
     * size. A thumbnail's width and height should not exceed 320. Ignored if the file is not uploaded
     * using multipart/form-data. Thumbnails can't be reused and can be only uploaded as a new file, so
     * you can pass “attach://\<file_attach_name\>” if the thumbnail was uploaded using
     * multipart/form-data under \<file_attach_name\>. [More information on Sending Files
     * ](https://core.telegram.org/bots/api/#sending-files)
     * @param caption Video caption (may also be used when resending videos by *file_id*), 0-1024
     * characters after entities parsing
     * @param parseMode Mode for parsing entities in the video caption. See [formatting
     * options](https://core.telegram.org/bots/api/#formatting-options) for more details.
     * @param captionEntities A JSON-serialized list of special entities that appear in the caption,
     * which can be specified instead of *parse_mode*
     * @param showCaptionAboveMedia Pass *True*, if the caption must be shown above the message
     * media
     * @param hasSpoiler Pass *True* if the video needs to be covered with a spoiler animation
     * @param supportsStreaming Pass *True* if the uploaded video is suitable for streaming
     * @param disableNotification Sends the message
     * [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a
     * notification with no sound.
     * @param protectContent Protects the contents of the sent message from forwarding and saving
     * @param messageEffectId Unique identifier of the message effect to be added to the message;
     * for private chats only
     * @param replyParameters Description of the message to reply to
     * @param replyMarkup Additional interface options. A JSON-serialized object for an [inline
     * keyboard](https://core.telegram.org/bots/features#inline-keyboards), [custom reply
     * keyboard](https://core.telegram.org/bots/features#keyboards), instructions to remove a reply
     * keyboard or to force a reply from the user
     */
    public suspend fun sendVideo(
        chatId: String,
        video: Input,
        businessConnectionId: String? = null,
        messageThreadId: Long? = null,
        duration: Int? = null,
        width: Int? = null,
        height: Int? = null,
        thumbnail: ContentInput? = null,
        caption: String? = null,
        parseMode: String? = null,
        captionEntities: Iterable<MessageEntity>? = null,
        showCaptionAboveMedia: Boolean? = null,
        hasSpoiler: Boolean? = null,
        supportsStreaming: Boolean? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        messageEffectId: String? = null,
        replyParameters: ReplyParameters? = null,
        replyMarkup: ReplyMarkup? = null,
    ): Message

    /**
     * Use this method to send animation files (GIF or H.264/MPEG-4 AVC video without sound). On
     * success, the sent [Message](https://core.telegram.org/bots/api/#message) is returned. Bots can
     * currently send animation files of up to 50 MB in size, this limit may be changed in the future.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the
     * format `@channelusername`)
     * @param animation Animation to send. Pass a file_id as String to send an animation that exists
     * on the Telegram servers (recommended), pass an HTTP URL as a String for Telegram to get an
     * animation from the Internet, or upload a new animation using multipart/form-data. [More
     * information on Sending Files ](https://core.telegram.org/bots/api/#sending-files)
     * @param businessConnectionId Unique identifier of the business connection on behalf of which
     * the message will be sent
     * @param messageThreadId Unique identifier for the target message thread (topic) of the forum;
     * for forum supergroups only
     * @param duration Duration of sent animation in seconds
     * @param width Animation width
     * @param height Animation height
     * @param thumbnail Thumbnail of the file sent; can be ignored if thumbnail generation for the
     * file is supported server-side. The thumbnail should be in JPEG format and less than 200 kB in
     * size. A thumbnail's width and height should not exceed 320. Ignored if the file is not uploaded
     * using multipart/form-data. Thumbnails can't be reused and can be only uploaded as a new file, so
     * you can pass “attach://\<file_attach_name\>” if the thumbnail was uploaded using
     * multipart/form-data under \<file_attach_name\>. [More information on Sending Files
     * ](https://core.telegram.org/bots/api/#sending-files)
     * @param caption Animation caption (may also be used when resending animation by *file_id*),
     * 0-1024 characters after entities parsing
     * @param parseMode Mode for parsing entities in the animation caption. See [formatting
     * options](https://core.telegram.org/bots/api/#formatting-options) for more details.
     * @param captionEntities A JSON-serialized list of special entities that appear in the caption,
     * which can be specified instead of *parse_mode*
     * @param showCaptionAboveMedia Pass *True*, if the caption must be shown above the message
     * media
     * @param hasSpoiler Pass *True* if the animation needs to be covered with a spoiler animation
     * @param disableNotification Sends the message
     * [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a
     * notification with no sound.
     * @param protectContent Protects the contents of the sent message from forwarding and saving
     * @param messageEffectId Unique identifier of the message effect to be added to the message;
     * for private chats only
     * @param replyParameters Description of the message to reply to
     * @param replyMarkup Additional interface options. A JSON-serialized object for an [inline
     * keyboard](https://core.telegram.org/bots/features#inline-keyboards), [custom reply
     * keyboard](https://core.telegram.org/bots/features#keyboards), instructions to remove a reply
     * keyboard or to force a reply from the user
     */
    public suspend fun sendAnimation(
        chatId: String,
        animation: Input,
        businessConnectionId: String? = null,
        messageThreadId: Long? = null,
        duration: Int? = null,
        width: Int? = null,
        height: Int? = null,
        thumbnail: ContentInput? = null,
        caption: String? = null,
        parseMode: String? = null,
        captionEntities: Iterable<MessageEntity>? = null,
        showCaptionAboveMedia: Boolean? = null,
        hasSpoiler: Boolean? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        messageEffectId: String? = null,
        replyParameters: ReplyParameters? = null,
        replyMarkup: ReplyMarkup? = null,
    ): Message

    /**
     * Use this method to send audio files, if you want Telegram clients to display the file as a
     * playable voice message. For this to work, your audio must be in an .OGG file encoded with OPUS,
     * or in .MP3 format, or in .M4A format (other formats may be sent as
     * [Audio](https://core.telegram.org/bots/api/#audio) or
     * [Document](https://core.telegram.org/bots/api/#document)). On success, the sent
     * [Message](https://core.telegram.org/bots/api/#message) is returned. Bots can currently send
     * voice messages of up to 50 MB in size, this limit may be changed in the future.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the
     * format `@channelusername`)
     * @param voice Audio file to send. Pass a file_id as String to send a file that exists on the
     * Telegram servers (recommended), pass an HTTP URL as a String for Telegram to get a file from the
     * Internet, or upload a new one using multipart/form-data. [More information on Sending Files
     * ](https://core.telegram.org/bots/api/#sending-files)
     * @param businessConnectionId Unique identifier of the business connection on behalf of which
     * the message will be sent
     * @param messageThreadId Unique identifier for the target message thread (topic) of the forum;
     * for forum supergroups only
     * @param caption Voice message caption, 0-1024 characters after entities parsing
     * @param parseMode Mode for parsing entities in the voice message caption. See [formatting
     * options](https://core.telegram.org/bots/api/#formatting-options) for more details.
     * @param captionEntities A JSON-serialized list of special entities that appear in the caption,
     * which can be specified instead of *parse_mode*
     * @param duration Duration of the voice message in seconds
     * @param disableNotification Sends the message
     * [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a
     * notification with no sound.
     * @param protectContent Protects the contents of the sent message from forwarding and saving
     * @param messageEffectId Unique identifier of the message effect to be added to the message;
     * for private chats only
     * @param replyParameters Description of the message to reply to
     * @param replyMarkup Additional interface options. A JSON-serialized object for an [inline
     * keyboard](https://core.telegram.org/bots/features#inline-keyboards), [custom reply
     * keyboard](https://core.telegram.org/bots/features#keyboards), instructions to remove a reply
     * keyboard or to force a reply from the user
     */
    public suspend fun sendVoice(
        chatId: String,
        voice: Input,
        businessConnectionId: String? = null,
        messageThreadId: Long? = null,
        caption: String? = null,
        parseMode: String? = null,
        captionEntities: Iterable<MessageEntity>? = null,
        duration: Int? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        messageEffectId: String? = null,
        replyParameters: ReplyParameters? = null,
        replyMarkup: ReplyMarkup? = null,
    ): Message

    /**
     * As of [v.4.0](https://telegram.org/blog/video-messages-and-telescope), Telegram clients
     * support rounded square MPEG4 videos of up to 1 minute long. Use this method to send video
     * messages. On success, the sent [Message](https://core.telegram.org/bots/api/#message) is
     * returned.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the
     * format `@channelusername`)
     * @param videoNote Video note to send. Pass a file_id as String to send a video note that
     * exists on the Telegram servers (recommended) or upload a new video using multipart/form-data.
     * [More information on Sending Files ](https://core.telegram.org/bots/api/#sending-files). Sending
     * video notes by a URL is currently unsupported
     * @param businessConnectionId Unique identifier of the business connection on behalf of which
     * the message will be sent
     * @param messageThreadId Unique identifier for the target message thread (topic) of the forum;
     * for forum supergroups only
     * @param duration Duration of sent video in seconds
     * @param length Video width and height, i.e. diameter of the video message
     * @param thumbnail Thumbnail of the file sent; can be ignored if thumbnail generation for the
     * file is supported server-side. The thumbnail should be in JPEG format and less than 200 kB in
     * size. A thumbnail's width and height should not exceed 320. Ignored if the file is not uploaded
     * using multipart/form-data. Thumbnails can't be reused and can be only uploaded as a new file, so
     * you can pass “attach://\<file_attach_name\>” if the thumbnail was uploaded using
     * multipart/form-data under \<file_attach_name\>. [More information on Sending Files
     * ](https://core.telegram.org/bots/api/#sending-files)
     * @param disableNotification Sends the message
     * [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a
     * notification with no sound.
     * @param protectContent Protects the contents of the sent message from forwarding and saving
     * @param messageEffectId Unique identifier of the message effect to be added to the message;
     * for private chats only
     * @param replyParameters Description of the message to reply to
     * @param replyMarkup Additional interface options. A JSON-serialized object for an [inline
     * keyboard](https://core.telegram.org/bots/features#inline-keyboards), [custom reply
     * keyboard](https://core.telegram.org/bots/features#keyboards), instructions to remove a reply
     * keyboard or to force a reply from the user
     */
    public suspend fun sendVideoNote(
        chatId: String,
        videoNote: Input,
        businessConnectionId: String? = null,
        messageThreadId: Long? = null,
        duration: Int? = null,
        length: Int? = null,
        thumbnail: ContentInput? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        messageEffectId: String? = null,
        replyParameters: ReplyParameters? = null,
        replyMarkup: ReplyMarkup? = null,
    ): Message

    /**
     * Use this method to send a group of photos, videos, documents or audios as an album. Documents
     * and audio files can be only grouped in an album with messages of the same type. On success, an
     * array of [Messages](https://core.telegram.org/bots/api/#message) that were sent is returned.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the
     * format `@channelusername`)
     * @param media A JSON-serialized array describing messages to be sent, must include 2-10 items
     * @param businessConnectionId Unique identifier of the business connection on behalf of which
     * the message will be sent
     * @param messageThreadId Unique identifier for the target message thread (topic) of the forum;
     * for forum supergroups only
     * @param disableNotification Sends messages
     * [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a
     * notification with no sound.
     * @param protectContent Protects the contents of the sent messages from forwarding and saving
     * @param messageEffectId Unique identifier of the message effect to be added to the message;
     * for private chats only
     * @param replyParameters Description of the message to reply to
     */
    public suspend fun sendMediaGroup(
        chatId: String,
        media: Iterable<InputMedia>,
        businessConnectionId: String? = null,
        messageThreadId: Long? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        messageEffectId: String? = null,
        replyParameters: ReplyParameters? = null,
    ): List<Message>

    /**
     * Use this method to send point on the map. On success, the sent
     * [Message](https://core.telegram.org/bots/api/#message) is returned.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the
     * format `@channelusername`)
     * @param latitude Latitude of the location
     * @param longitude Longitude of the location
     * @param businessConnectionId Unique identifier of the business connection on behalf of which
     * the message will be sent
     * @param messageThreadId Unique identifier for the target message thread (topic) of the forum;
     * for forum supergroups only
     * @param horizontalAccuracy The radius of uncertainty for the location, measured in meters;
     * 0-1500
     * @param livePeriod Period in seconds during which the location will be updated (see [Live
     * Locations](https://telegram.org/blog/live-locations), should be between 60 and 86400, or
     * 0x7FFFFFFF for live locations that can be edited indefinitely.
     * @param heading For live locations, a direction in which the user is moving, in degrees. Must
     * be between 1 and 360 if specified.
     * @param proximityAlertRadius For live locations, a maximum distance for proximity alerts about
     * approaching another chat member, in meters. Must be between 1 and 100000 if specified.
     * @param disableNotification Sends the message
     * [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a
     * notification with no sound.
     * @param protectContent Protects the contents of the sent message from forwarding and saving
     * @param messageEffectId Unique identifier of the message effect to be added to the message;
     * for private chats only
     * @param replyParameters Description of the message to reply to
     * @param replyMarkup Additional interface options. A JSON-serialized object for an [inline
     * keyboard](https://core.telegram.org/bots/features#inline-keyboards), [custom reply
     * keyboard](https://core.telegram.org/bots/features#keyboards), instructions to remove a reply
     * keyboard or to force a reply from the user
     */
    public suspend fun sendLocation(
        chatId: String,
        latitude: Double,
        longitude: Double,
        businessConnectionId: String? = null,
        messageThreadId: Long? = null,
        horizontalAccuracy: Double? = null,
        livePeriod: Int? = null,
        heading: Int? = null,
        proximityAlertRadius: Int? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        messageEffectId: String? = null,
        replyParameters: ReplyParameters? = null,
        replyMarkup: ReplyMarkup? = null,
    ): Message

    /**
     * Use this method to send information about a venue. On success, the sent
     * [Message](https://core.telegram.org/bots/api/#message) is returned.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the
     * format `@channelusername`)
     * @param latitude Latitude of the venue
     * @param longitude Longitude of the venue
     * @param title Name of the venue
     * @param address Address of the venue
     * @param businessConnectionId Unique identifier of the business connection on behalf of which
     * the message will be sent
     * @param messageThreadId Unique identifier for the target message thread (topic) of the forum;
     * for forum supergroups only
     * @param foursquareId Foursquare identifier of the venue
     * @param foursquareType Foursquare type of the venue, if known. (For example,
     * “arts_entertainment/default”, “arts_entertainment/aquarium” or “food/icecream”.)
     * @param googlePlaceId Google Places identifier of the venue
     * @param googlePlaceType Google Places type of the venue. (See [supported
     * types](https://developers.google.com/places/web-service/supported_types).)
     * @param disableNotification Sends the message
     * [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a
     * notification with no sound.
     * @param protectContent Protects the contents of the sent message from forwarding and saving
     * @param messageEffectId Unique identifier of the message effect to be added to the message;
     * for private chats only
     * @param replyParameters Description of the message to reply to
     * @param replyMarkup Additional interface options. A JSON-serialized object for an [inline
     * keyboard](https://core.telegram.org/bots/features#inline-keyboards), [custom reply
     * keyboard](https://core.telegram.org/bots/features#keyboards), instructions to remove a reply
     * keyboard or to force a reply from the user
     */
    public suspend fun sendVenue(
        chatId: String,
        latitude: Double,
        longitude: Double,
        title: String,
        address: String,
        businessConnectionId: String? = null,
        messageThreadId: Long? = null,
        foursquareId: String? = null,
        foursquareType: String? = null,
        googlePlaceId: String? = null,
        googlePlaceType: String? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        messageEffectId: String? = null,
        replyParameters: ReplyParameters? = null,
        replyMarkup: ReplyMarkup? = null,
    ): Message

    /**
     * Use this method to send phone contacts. On success, the sent
     * [Message](https://core.telegram.org/bots/api/#message) is returned.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the
     * format `@channelusername`)
     * @param phoneNumber Contact's phone number
     * @param firstName Contact's first name
     * @param businessConnectionId Unique identifier of the business connection on behalf of which
     * the message will be sent
     * @param messageThreadId Unique identifier for the target message thread (topic) of the forum;
     * for forum supergroups only
     * @param lastName Contact's last name
     * @param vcard Additional data about the contact in the form of a
     * [vCard](https://en.wikipedia.org/wiki/VCard), 0-2048 bytes
     * @param disableNotification Sends the message
     * [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a
     * notification with no sound.
     * @param protectContent Protects the contents of the sent message from forwarding and saving
     * @param messageEffectId Unique identifier of the message effect to be added to the message;
     * for private chats only
     * @param replyParameters Description of the message to reply to
     * @param replyMarkup Additional interface options. A JSON-serialized object for an [inline
     * keyboard](https://core.telegram.org/bots/features#inline-keyboards), [custom reply
     * keyboard](https://core.telegram.org/bots/features#keyboards), instructions to remove a reply
     * keyboard or to force a reply from the user
     */
    public suspend fun sendContact(
        chatId: String,
        phoneNumber: String,
        firstName: String,
        businessConnectionId: String? = null,
        messageThreadId: Long? = null,
        lastName: String? = null,
        vcard: String? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        messageEffectId: String? = null,
        replyParameters: ReplyParameters? = null,
        replyMarkup: ReplyMarkup? = null,
    ): Message

    /**
     * Use this method to send a native poll. On success, the sent
     * [Message](https://core.telegram.org/bots/api/#message) is returned.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the
     * format `@channelusername`)
     * @param question Poll question, 1-300 characters
     * @param options A JSON-serialized list of 2-10 answer options
     * @param businessConnectionId Unique identifier of the business connection on behalf of which
     * the message will be sent
     * @param messageThreadId Unique identifier for the target message thread (topic) of the forum;
     * for forum supergroups only
     * @param questionParseMode Mode for parsing entities in the question. See [formatting
     * options](https://core.telegram.org/bots/api/#formatting-options) for more details. Currently,
     * only custom emoji entities are allowed
     * @param questionEntities A JSON-serialized list of special entities that appear in the poll
     * question. It can be specified instead of *question_parse_mode*
     * @param isAnonymous *True*, if the poll needs to be anonymous, defaults to *True*
     * @param type Poll type, “quiz” or “regular”, defaults to “regular”
     * @param allowsMultipleAnswers *True*, if the poll allows multiple answers, ignored for polls
     * in quiz mode, defaults to *False*
     * @param correctOptionId 0-based identifier of the correct answer option, required for polls in
     * quiz mode
     * @param explanation Text that is shown when a user chooses an incorrect answer or taps on the
     * lamp icon in a quiz-style poll, 0-200 characters with at most 2 line feeds after entities
     * parsing
     * @param explanationParseMode Mode for parsing entities in the explanation. See [formatting
     * options](https://core.telegram.org/bots/api/#formatting-options) for more details.
     * @param explanationEntities A JSON-serialized list of special entities that appear in the poll
     * explanation. It can be specified instead of *explanation_parse_mode*
     * @param openPeriod Amount of time in seconds the poll will be active after creation, 5-600.
     * Can't be used together with *close_date*.
     * @param closeDate Point in time (Unix timestamp) when the poll will be automatically closed.
     * Must be at least 5 and no more than 600 seconds in the future. Can't be used together with
     * *open_period*.
     * @param isClosed Pass *True* if the poll needs to be immediately closed. This can be useful
     * for poll preview.
     * @param disableNotification Sends the message
     * [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a
     * notification with no sound.
     * @param protectContent Protects the contents of the sent message from forwarding and saving
     * @param messageEffectId Unique identifier of the message effect to be added to the message;
     * for private chats only
     * @param replyParameters Description of the message to reply to
     * @param replyMarkup Additional interface options. A JSON-serialized object for an [inline
     * keyboard](https://core.telegram.org/bots/features#inline-keyboards), [custom reply
     * keyboard](https://core.telegram.org/bots/features#keyboards), instructions to remove a reply
     * keyboard or to force a reply from the user
     */
    public suspend fun sendPoll(
        chatId: String,
        question: String,
        options: Iterable<InputPollOption>,
        businessConnectionId: String? = null,
        messageThreadId: Long? = null,
        questionParseMode: String? = null,
        questionEntities: Iterable<MessageEntity>? = null,
        isAnonymous: Boolean? = null,
        type: String? = null,
        allowsMultipleAnswers: Boolean? = null,
        correctOptionId: Long? = null,
        explanation: String? = null,
        explanationParseMode: String? = null,
        explanationEntities: Iterable<MessageEntity>? = null,
        openPeriod: Int? = null,
        closeDate: Long? = null,
        isClosed: Boolean? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        messageEffectId: String? = null,
        replyParameters: ReplyParameters? = null,
        replyMarkup: ReplyMarkup? = null,
    ): Message

    /**
     * Use this method to send an animated emoji that will display a random value. On success, the
     * sent [Message](https://core.telegram.org/bots/api/#message) is returned.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the
     * format `@channelusername`)
     * @param businessConnectionId Unique identifier of the business connection on behalf of which
     * the message will be sent
     * @param messageThreadId Unique identifier for the target message thread (topic) of the forum;
     * for forum supergroups only
     * @param emoji Emoji on which the dice throw animation is based. Currently, must be one of
     * “🎲”, “🎯”, “🏀”, “⚽”, “🎳”, or “🎰”. Dice can have values 1-6 for “🎲”, “🎯” and “🎳”, values
     * 1-5 for “🏀” and “⚽”, and values 1-64 for “🎰”. Defaults to “🎲”
     * @param disableNotification Sends the message
     * [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a
     * notification with no sound.
     * @param protectContent Protects the contents of the sent message from forwarding
     * @param messageEffectId Unique identifier of the message effect to be added to the message;
     * for private chats only
     * @param replyParameters Description of the message to reply to
     * @param replyMarkup Additional interface options. A JSON-serialized object for an [inline
     * keyboard](https://core.telegram.org/bots/features#inline-keyboards), [custom reply
     * keyboard](https://core.telegram.org/bots/features#keyboards), instructions to remove a reply
     * keyboard or to force a reply from the user
     */
    public suspend fun sendDice(
        chatId: String,
        businessConnectionId: String? = null,
        messageThreadId: Long? = null,
        emoji: String? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        messageEffectId: String? = null,
        replyParameters: ReplyParameters? = null,
        replyMarkup: ReplyMarkup? = null,
    ): Message

    /**
     * Use this method when you need to tell the user that something is happening on the bot's side.
     * The status is set for 5 seconds or less (when a message arrives from your bot, Telegram clients
     * clear its typing status). Returns *True* on success.
     *
     * Example: The [ImageBot](https://t.me/imagebot) needs some time to process a request and
     * upload the image. Instead of sending a text message along the lines of “Retrieving image, please
     * wait…”, the bot may use [sendChatAction](https://core.telegram.org/bots/api/#sendchataction)
     * with *action* = *upload_photo*. The user will see a “sending photo” status for the bot.
     *
     * We only recommend using this method when a response from the bot will take a **noticeable**
     * amount of time to arrive.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the
     * format `@channelusername`)
     * @param action Type of action to broadcast. Choose one, depending on what the user is about to
     * receive: *typing* for [text messages](https://core.telegram.org/bots/api/#sendmessage),
     * *upload_photo* for [photos](https://core.telegram.org/bots/api/#sendphoto), *record_video* or
     * *upload_video* for [videos](https://core.telegram.org/bots/api/#sendvideo), *record_voice* or
     * *upload_voice* for [voice notes](https://core.telegram.org/bots/api/#sendvoice),
     * *upload_document* for [general files](https://core.telegram.org/bots/api/#senddocument),
     * *choose_sticker* for [stickers](https://core.telegram.org/bots/api/#sendsticker),
     * *find_location* for [location data](https://core.telegram.org/bots/api/#sendlocation),
     * *record_video_note* or *upload_video_note* for [video
     * notes](https://core.telegram.org/bots/api/#sendvideonote).
     * @param businessConnectionId Unique identifier of the business connection on behalf of which
     * the action will be sent
     * @param messageThreadId Unique identifier for the target message thread; for supergroups only
     */
    public suspend fun sendChatAction(
        chatId: String,
        action: String,
        businessConnectionId: String? = null,
        messageThreadId: Long? = null,
    ): Boolean

    /**
     * Use this method to change the chosen reactions on a message. Service messages can't be
     * reacted to. Automatically forwarded messages from a channel to its discussion group have the
     * same available reactions as messages in the channel. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the
     * format `@channelusername`)
     * @param messageId Identifier of the target message. If the message belongs to a media group,
     * the reaction is set to the first non-deleted message in the group instead.
     * @param reaction A JSON-serialized list of reaction types to set on the message. Currently, as
     * non-premium users, bots can set up to one reaction per message. A custom emoji reaction can be
     * used if it is either already present on the message or explicitly allowed by chat
     * administrators.
     * @param isBig Pass *True* to set the reaction with a big animation
     */
    public suspend fun setMessageReaction(
        chatId: String,
        messageId: Long,
        reaction: Iterable<ReactionType>? = null,
        isBig: Boolean? = null,
    ): Boolean

    /**
     * Use this method to get a list of profile pictures for a user. Returns a
     * [UserProfilePhotos](https://core.telegram.org/bots/api/#userprofilephotos) object.
     *
     * @param userId Unique identifier of the target user
     * @param offset Sequential number of the first photo to be returned. By default, all photos are
     * returned.
     * @param limit Limits the number of photos to be retrieved. Values between 1-100 are accepted.
     * Defaults to 100.
     */
    public suspend fun getUserProfilePhotos(
        userId: Long,
        offset: Int? = null,
        limit: Int? = null,
    ): UserProfilePhotos

    /**
     * Use this method to get basic information about a file and prepare it for downloading. For the
     * moment, bots can download files of up to 20MB in size. On success, a
     * [File](https://core.telegram.org/bots/api/#file) object is returned. The file can then be
     * downloaded via the link `https://api.telegram.org/file/bot<token>/<file_path>`, where
     * `<file_path>` is taken from the response. It is guaranteed that the link will be valid for at
     * least 1 hour. When the link expires, a new one can be requested by calling
     * [getFile](https://core.telegram.org/bots/api/#getfile) again.
     *
     * @param fileId File identifier to get information about
     */
    public suspend fun getFile(fileId: String): File

    /**
     * Use this method to ban a user in a group, a supergroup or a channel. In the case of
     * supergroups and channels, the user will not be able to return to the chat on their own using
     * invite links, etc., unless [unbanned](https://core.telegram.org/bots/api/#unbanchatmember)
     * first. The bot must be an administrator in the chat for this to work and must have the
     * appropriate administrator rights. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target group or username of the target supergroup or
     * channel (in the format `@channelusername`)
     * @param userId Unique identifier of the target user
     * @param untilDate Date when the user will be unbanned; Unix time. If user is banned for more
     * than 366 days or less than 30 seconds from the current time they are considered to be banned
     * forever. Applied for supergroups and channels only.
     * @param revokeMessages Pass *True* to delete all messages from the chat for the user that is
     * being removed. If *False*, the user will be able to see messages in the group that were sent
     * before the user was removed. Always *True* for supergroups and channels.
     */
    public suspend fun banChatMember(
        chatId: String,
        userId: Long,
        untilDate: Long? = null,
        revokeMessages: Boolean? = null,
    ): Boolean

    /**
     * Use this method to unban a previously banned user in a supergroup or channel. The user will
     * **not** return to the group or channel automatically, but will be able to join via link, etc.
     * The bot must be an administrator for this to work. By default, this method guarantees that after
     * the call the user is not a member of the chat, but will be able to join it. So if the user is a
     * member of the chat they will also be **removed** from the chat. If you don't want this, use the
     * parameter *only_if_banned*. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target group or username of the target supergroup or
     * channel (in the format `@channelusername`)
     * @param userId Unique identifier of the target user
     * @param onlyIfBanned Do nothing if the user is not banned
     */
    public suspend fun unbanChatMember(
        chatId: String,
        userId: Long,
        onlyIfBanned: Boolean? = null,
    ): Boolean

    /**
     * Use this method to restrict a user in a supergroup. The bot must be an administrator in the
     * supergroup for this to work and must have the appropriate administrator rights. Pass *True* for
     * all permissions to lift restrictions from a user. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup (in
     * the format `@supergroupusername`)
     * @param userId Unique identifier of the target user
     * @param permissions A JSON-serialized object for new user permissions
     * @param useIndependentChatPermissions Pass *True* if chat permissions are set independently.
     * Otherwise, the *can_send_other_messages* and *can_add_web_page_previews* permissions will imply
     * the *can_send_messages*, *can_send_audios*, *can_send_documents*, *can_send_photos*,
     * *can_send_videos*, *can_send_video_notes*, and *can_send_voice_notes* permissions; the
     * *can_send_polls* permission will imply the *can_send_messages* permission.
     * @param untilDate Date when restrictions will be lifted for the user; Unix time. If user is
     * restricted for more than 366 days or less than 30 seconds from the current time, they are
     * considered to be restricted forever
     */
    public suspend fun restrictChatMember(
        chatId: String,
        userId: Long,
        permissions: ChatPermissions,
        useIndependentChatPermissions: Boolean? = null,
        untilDate: Long? = null,
    ): Boolean

    /**
     * Use this method to promote or demote a user in a supergroup or a channel. The bot must be an
     * administrator in the chat for this to work and must have the appropriate administrator rights.
     * Pass *False* for all boolean parameters to demote a user. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the
     * format `@channelusername`)
     * @param userId Unique identifier of the target user
     * @param isAnonymous Pass *True* if the administrator's presence in the chat is hidden
     * @param canManageChat Pass *True* if the administrator can access the chat event log, get
     * boost list, see hidden supergroup and channel members, report spam messages and ignore slow
     * mode. Implied by any other administrator privilege.
     * @param canDeleteMessages Pass *True* if the administrator can delete messages of other users
     * @param canManageVideoChats Pass *True* if the administrator can manage video chats
     * @param canRestrictMembers Pass *True* if the administrator can restrict, ban or unban chat
     * members, or access supergroup statistics
     * @param canPromoteMembers Pass *True* if the administrator can add new administrators with a
     * subset of their own privileges or demote administrators that they have promoted, directly or
     * indirectly (promoted by administrators that were appointed by him)
     * @param canChangeInfo Pass *True* if the administrator can change chat title, photo and other
     * settings
     * @param canInviteUsers Pass *True* if the administrator can invite new users to the chat
     * @param canPostStories Pass *True* if the administrator can post stories to the chat
     * @param canEditStories Pass *True* if the administrator can edit stories posted by other
     * users, post stories to the chat page, pin chat stories, and access the chat's story archive
     * @param canDeleteStories Pass *True* if the administrator can delete stories posted by other
     * users
     * @param canPostMessages Pass *True* if the administrator can post messages in the channel, or
     * access channel statistics; for channels only
     * @param canEditMessages Pass *True* if the administrator can edit messages of other users and
     * can pin messages; for channels only
     * @param canPinMessages Pass *True* if the administrator can pin messages; for supergroups only
     * @param canManageTopics Pass *True* if the user is allowed to create, rename, close, and
     * reopen forum topics; for supergroups only
     */
    public suspend fun promoteChatMember(
        chatId: String,
        userId: Long,
        isAnonymous: Boolean? = null,
        canManageChat: Boolean? = null,
        canDeleteMessages: Boolean? = null,
        canManageVideoChats: Boolean? = null,
        canRestrictMembers: Boolean? = null,
        canPromoteMembers: Boolean? = null,
        canChangeInfo: Boolean? = null,
        canInviteUsers: Boolean? = null,
        canPostStories: Boolean? = null,
        canEditStories: Boolean? = null,
        canDeleteStories: Boolean? = null,
        canPostMessages: Boolean? = null,
        canEditMessages: Boolean? = null,
        canPinMessages: Boolean? = null,
        canManageTopics: Boolean? = null,
    ): Boolean

    /**
     * Use this method to set a custom title for an administrator in a supergroup promoted by the
     * bot. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup (in
     * the format `@supergroupusername`)
     * @param userId Unique identifier of the target user
     * @param customTitle New custom title for the administrator; 0-16 characters, emoji are not
     * allowed
     */
    public suspend fun setChatAdministratorCustomTitle(
        chatId: String,
        userId: Long,
        customTitle: String,
    ): Boolean

    /**
     * Use this method to ban a channel chat in a supergroup or a channel. Until the chat is
     * [unbanned](https://core.telegram.org/bots/api/#unbanchatsenderchat), the owner of the banned
     * chat won't be able to send messages on behalf of **any of their channels**. The bot must be an
     * administrator in the supergroup or channel for this to work and must have the appropriate
     * administrator rights. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the
     * format `@channelusername`)
     * @param senderChatId Unique identifier of the target sender chat
     */
    public suspend fun banChatSenderChat(chatId: String, senderChatId: Long): Boolean

    /**
     * Use this method to unban a previously banned channel chat in a supergroup or channel. The bot
     * must be an administrator for this to work and must have the appropriate administrator rights.
     * Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the
     * format `@channelusername`)
     * @param senderChatId Unique identifier of the target sender chat
     */
    public suspend fun unbanChatSenderChat(chatId: String, senderChatId: Long): Boolean

    /**
     * Use this method to set default chat permissions for all members. The bot must be an
     * administrator in the group or a supergroup for this to work and must have the
     * *can_restrict_members* administrator rights. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup (in
     * the format `@supergroupusername`)
     * @param permissions A JSON-serialized object for new default chat permissions
     * @param useIndependentChatPermissions Pass *True* if chat permissions are set independently.
     * Otherwise, the *can_send_other_messages* and *can_add_web_page_previews* permissions will imply
     * the *can_send_messages*, *can_send_audios*, *can_send_documents*, *can_send_photos*,
     * *can_send_videos*, *can_send_video_notes*, and *can_send_voice_notes* permissions; the
     * *can_send_polls* permission will imply the *can_send_messages* permission.
     */
    public suspend fun setChatPermissions(
        chatId: String,
        permissions: ChatPermissions,
        useIndependentChatPermissions: Boolean? = null,
    ): Boolean

    /**
     * Use this method to generate a new primary invite link for a chat; any previously generated
     * primary link is revoked. The bot must be an administrator in the chat for this to work and must
     * have the appropriate administrator rights. Returns the new invite link as *String* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the
     * format `@channelusername`)
     */
    public suspend fun exportChatInviteLink(chatId: String): String

    /**
     * Use this method to create an additional invite link for a chat. The bot must be an
     * administrator in the chat for this to work and must have the appropriate administrator rights.
     * The link can be revoked using the method
     * [revokeChatInviteLink](https://core.telegram.org/bots/api/#revokechatinvitelink). Returns the
     * new invite link as [ChatInviteLink](https://core.telegram.org/bots/api/#chatinvitelink) object.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the
     * format `@channelusername`)
     * @param name Invite link name; 0-32 characters
     * @param expireDate Point in time (Unix timestamp) when the link will expire
     * @param memberLimit The maximum number of users that can be members of the chat simultaneously
     * after joining the chat via this invite link; 1-99999
     * @param createsJoinRequest *True*, if users joining the chat via the link need to be approved
     * by chat administrators. If *True*, *member_limit* can't be specified
     */
    public suspend fun createChatInviteLink(
        chatId: String,
        name: String? = null,
        expireDate: Long? = null,
        memberLimit: Int? = null,
        createsJoinRequest: Boolean? = null,
    ): ChatInviteLink

    /**
     * Use this method to edit a non-primary invite link created by the bot. The bot must be an
     * administrator in the chat for this to work and must have the appropriate administrator rights.
     * Returns the edited invite link as a
     * [ChatInviteLink](https://core.telegram.org/bots/api/#chatinvitelink) object.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the
     * format `@channelusername`)
     * @param inviteLink The invite link to edit
     * @param name Invite link name; 0-32 characters
     * @param expireDate Point in time (Unix timestamp) when the link will expire
     * @param memberLimit The maximum number of users that can be members of the chat simultaneously
     * after joining the chat via this invite link; 1-99999
     * @param createsJoinRequest *True*, if users joining the chat via the link need to be approved
     * by chat administrators. If *True*, *member_limit* can't be specified
     */
    public suspend fun editChatInviteLink(
        chatId: String,
        inviteLink: String,
        name: String? = null,
        expireDate: Long? = null,
        memberLimit: Int? = null,
        createsJoinRequest: Boolean? = null,
    ): ChatInviteLink

    /**
     * Use this method to revoke an invite link created by the bot. If the primary link is revoked,
     * a new link is automatically generated. The bot must be an administrator in the chat for this to
     * work and must have the appropriate administrator rights. Returns the revoked invite link as
     * [ChatInviteLink](https://core.telegram.org/bots/api/#chatinvitelink) object.
     *
     * @param chatId Unique identifier of the target chat or username of the target channel (in the
     * format `@channelusername`)
     * @param inviteLink The invite link to revoke
     */
    public suspend fun revokeChatInviteLink(chatId: String, inviteLink: String): ChatInviteLink

    /**
     * Use this method to approve a chat join request. The bot must be an administrator in the chat
     * for this to work and must have the *can_invite_users* administrator right. Returns *True* on
     * success.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the
     * format `@channelusername`)
     * @param userId Unique identifier of the target user
     */
    public suspend fun approveChatJoinRequest(chatId: String, userId: Long): Boolean

    /**
     * Use this method to decline a chat join request. The bot must be an administrator in the chat
     * for this to work and must have the *can_invite_users* administrator right. Returns *True* on
     * success.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the
     * format `@channelusername`)
     * @param userId Unique identifier of the target user
     */
    public suspend fun declineChatJoinRequest(chatId: String, userId: Long): Boolean

    /**
     * Use this method to set a new profile photo for the chat. Photos can't be changed for private
     * chats. The bot must be an administrator in the chat for this to work and must have the
     * appropriate administrator rights. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the
     * format `@channelusername`)
     * @param photo New chat photo, uploaded using multipart/form-data
     */
    public suspend fun setChatPhoto(chatId: String, photo: Input): Boolean

    /**
     * Use this method to delete a chat photo. Photos can't be changed for private chats. The bot
     * must be an administrator in the chat for this to work and must have the appropriate
     * administrator rights. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the
     * format `@channelusername`)
     */
    public suspend fun deleteChatPhoto(chatId: String): Boolean

    /**
     * Use this method to change the title of a chat. Titles can't be changed for private chats. The
     * bot must be an administrator in the chat for this to work and must have the appropriate
     * administrator rights. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the
     * format `@channelusername`)
     * @param title New chat title, 1-128 characters
     */
    public suspend fun setChatTitle(chatId: String, title: String): Boolean

    /**
     * Use this method to change the description of a group, a supergroup or a channel. The bot must
     * be an administrator in the chat for this to work and must have the appropriate administrator
     * rights. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the
     * format `@channelusername`)
     * @param description New chat description, 0-255 characters
     */
    public suspend fun setChatDescription(chatId: String, description: String? = null): Boolean

    /**
     * Use this method to add a message to the list of pinned messages in a chat. If the chat is not
     * a private chat, the bot must be an administrator in the chat for this to work and must have the
     * 'can_pin_messages' administrator right in a supergroup or 'can_edit_messages' administrator
     * right in a channel. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the
     * format `@channelusername`)
     * @param messageId Identifier of a message to pin
     * @param disableNotification Pass *True* if it is not necessary to send a notification to all
     * chat members about the new pinned message. Notifications are always disabled in channels and
     * private chats.
     */
    public suspend fun pinChatMessage(
        chatId: String,
        messageId: Long,
        disableNotification: Boolean? = null,
    ): Boolean

    /**
     * Use this method to remove a message from the list of pinned messages in a chat. If the chat
     * is not a private chat, the bot must be an administrator in the chat for this to work and must
     * have the 'can_pin_messages' administrator right in a supergroup or 'can_edit_messages'
     * administrator right in a channel. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the
     * format `@channelusername`)
     * @param messageId Identifier of a message to unpin. If not specified, the most recent pinned
     * message (by sending date) will be unpinned.
     */
    public suspend fun unpinChatMessage(chatId: String, messageId: Long? = null): Boolean

    /**
     * Use this method to clear the list of pinned messages in a chat. If the chat is not a private
     * chat, the bot must be an administrator in the chat for this to work and must have the
     * 'can_pin_messages' administrator right in a supergroup or 'can_edit_messages' administrator
     * right in a channel. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the
     * format `@channelusername`)
     */
    public suspend fun unpinAllChatMessages(chatId: String): Boolean

    /**
     * Use this method for your bot to leave a group, supergroup or channel. Returns *True* on
     * success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup or
     * channel (in the format `@channelusername`)
     */
    public suspend fun leaveChat(chatId: String): Boolean

    /**
     * Use this method to get up-to-date information about the chat. Returns a
     * [ChatFullInfo](https://core.telegram.org/bots/api/#chatfullinfo) object on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup or
     * channel (in the format `@channelusername`)
     */
    public suspend fun getChat(chatId: String): ChatFullInfo

    /**
     * Use this method to get a list of administrators in a chat, which aren't bots. Returns an
     * Array of [ChatMember](https://core.telegram.org/bots/api/#chatmember) objects.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup or
     * channel (in the format `@channelusername`)
     */
    public suspend fun getChatAdministrators(chatId: String): List<ChatMember>

    /**
     * Use this method to get the number of members in a chat. Returns *Int* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup or
     * channel (in the format `@channelusername`)
     */
    public suspend fun getChatMemberCount(chatId: String): Int

    /**
     * Use this method to get information about a member of a chat. The method is only guaranteed to
     * work for other users if the bot is an administrator in the chat. Returns a
     * [ChatMember](https://core.telegram.org/bots/api/#chatmember) object on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup or
     * channel (in the format `@channelusername`)
     * @param userId Unique identifier of the target user
     */
    public suspend fun getChatMember(chatId: String, userId: Long): ChatMember

    /**
     * Use this method to set a new group sticker set for a supergroup. The bot must be an
     * administrator in the chat for this to work and must have the appropriate administrator rights.
     * Use the field *can_set_sticker_set* optionally returned in
     * [getChat](https://core.telegram.org/bots/api/#getchat) requests to check if the bot can use this
     * method. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup (in
     * the format `@supergroupusername`)
     * @param stickerSetName Name of the sticker set to be set as the group sticker set
     */
    public suspend fun setChatStickerSet(chatId: String, stickerSetName: String): Boolean

    /**
     * Use this method to delete a group sticker set from a supergroup. The bot must be an
     * administrator in the chat for this to work and must have the appropriate administrator rights.
     * Use the field *can_set_sticker_set* optionally returned in
     * [getChat](https://core.telegram.org/bots/api/#getchat) requests to check if the bot can use this
     * method. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup (in
     * the format `@supergroupusername`)
     */
    public suspend fun deleteChatStickerSet(chatId: String): Boolean

    /**
     * Use this method to get custom emoji stickers, which can be used as a forum topic icon by any
     * user. Requires no parameters. Returns an Array of
     * [Sticker](https://core.telegram.org/bots/api/#sticker) objects.
     */
    public suspend fun getForumTopicIconStickers(): List<Sticker>

    /**
     * Use this method to create a topic in a forum supergroup chat. The bot must be an
     * administrator in the chat for this to work and must have the *can_manage_topics* administrator
     * rights. Returns information about the created topic as a
     * [ForumTopic](https://core.telegram.org/bots/api/#forumtopic) object.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup (in
     * the format `@supergroupusername`)
     * @param name Topic name, 1-128 characters
     * @param iconColor Color of the topic icon in RGB format. Currently, must be one of 7322096
     * (0x6FB9F0), 16766590 (0xFFD67E), 13338331 (0xCB86DB), 9367192 (0x8EEE98), 16749490 (0xFF93B2),
     * or 16478047 (0xFB6F5F)
     * @param iconCustomEmojiId Unique identifier of the custom emoji shown as the topic icon. Use
     * [getForumTopicIconStickers](https://core.telegram.org/bots/api/#getforumtopiciconstickers) to
     * get all allowed custom emoji identifiers.
     */
    public suspend fun createForumTopic(
        chatId: String,
        name: String,
        iconColor: Int? = null,
        iconCustomEmojiId: String? = null,
    ): ForumTopic

    /**
     * Use this method to edit name and icon of a topic in a forum supergroup chat. The bot must be
     * an administrator in the chat for this to work and must have *can_manage_topics* administrator
     * rights, unless it is the creator of the topic. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup (in
     * the format `@supergroupusername`)
     * @param messageThreadId Unique identifier for the target message thread of the forum topic
     * @param name New topic name, 0-128 characters. If not specified or empty, the current name of
     * the topic will be kept
     * @param iconCustomEmojiId New unique identifier of the custom emoji shown as the topic icon.
     * Use [getForumTopicIconStickers](https://core.telegram.org/bots/api/#getforumtopiciconstickers)
     * to get all allowed custom emoji identifiers. Pass an empty string to remove the icon. If not
     * specified, the current icon will be kept
     */
    public suspend fun editForumTopic(
        chatId: String,
        messageThreadId: Long,
        name: String? = null,
        iconCustomEmojiId: String? = null,
    ): Boolean

    /**
     * Use this method to close an open topic in a forum supergroup chat. The bot must be an
     * administrator in the chat for this to work and must have the *can_manage_topics* administrator
     * rights, unless it is the creator of the topic. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup (in
     * the format `@supergroupusername`)
     * @param messageThreadId Unique identifier for the target message thread of the forum topic
     */
    public suspend fun closeForumTopic(chatId: String, messageThreadId: Long): Boolean

    /**
     * Use this method to reopen a closed topic in a forum supergroup chat. The bot must be an
     * administrator in the chat for this to work and must have the *can_manage_topics* administrator
     * rights, unless it is the creator of the topic. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup (in
     * the format `@supergroupusername`)
     * @param messageThreadId Unique identifier for the target message thread of the forum topic
     */
    public suspend fun reopenForumTopic(chatId: String, messageThreadId: Long): Boolean

    /**
     * Use this method to delete a forum topic along with all its messages in a forum supergroup
     * chat. The bot must be an administrator in the chat for this to work and must have the
     * *can_delete_messages* administrator rights. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup (in
     * the format `@supergroupusername`)
     * @param messageThreadId Unique identifier for the target message thread of the forum topic
     */
    public suspend fun deleteForumTopic(chatId: String, messageThreadId: Long): Boolean

    /**
     * Use this method to clear the list of pinned messages in a forum topic. The bot must be an
     * administrator in the chat for this to work and must have the *can_pin_messages* administrator
     * right in the supergroup. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup (in
     * the format `@supergroupusername`)
     * @param messageThreadId Unique identifier for the target message thread of the forum topic
     */
    public suspend fun unpinAllForumTopicMessages(chatId: String, messageThreadId: Long): Boolean

    /**
     * Use this method to edit the name of the 'General' topic in a forum supergroup chat. The bot
     * must be an administrator in the chat for this to work and must have *can_manage_topics*
     * administrator rights. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup (in
     * the format `@supergroupusername`)
     * @param name New topic name, 1-128 characters
     */
    public suspend fun editGeneralForumTopic(chatId: String, name: String): Boolean

    /**
     * Use this method to close an open 'General' topic in a forum supergroup chat. The bot must be
     * an administrator in the chat for this to work and must have the *can_manage_topics*
     * administrator rights. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup (in
     * the format `@supergroupusername`)
     */
    public suspend fun closeGeneralForumTopic(chatId: String): Boolean

    /**
     * Use this method to reopen a closed 'General' topic in a forum supergroup chat. The bot must
     * be an administrator in the chat for this to work and must have the *can_manage_topics*
     * administrator rights. The topic will be automatically unhidden if it was hidden. Returns *True*
     * on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup (in
     * the format `@supergroupusername`)
     */
    public suspend fun reopenGeneralForumTopic(chatId: String): Boolean

    /**
     * Use this method to hide the 'General' topic in a forum supergroup chat. The bot must be an
     * administrator in the chat for this to work and must have the *can_manage_topics* administrator
     * rights. The topic will be automatically closed if it was open. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup (in
     * the format `@supergroupusername`)
     */
    public suspend fun hideGeneralForumTopic(chatId: String): Boolean

    /**
     * Use this method to unhide the 'General' topic in a forum supergroup chat. The bot must be an
     * administrator in the chat for this to work and must have the *can_manage_topics* administrator
     * rights. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup (in
     * the format `@supergroupusername`)
     */
    public suspend fun unhideGeneralForumTopic(chatId: String): Boolean

    /**
     * Use this method to clear the list of pinned messages in a General forum topic. The bot must
     * be an administrator in the chat for this to work and must have the *can_pin_messages*
     * administrator right in the supergroup. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup (in
     * the format `@supergroupusername`)
     */
    public suspend fun unpinAllGeneralForumTopicMessages(chatId: String): Boolean

    /**
     * Use this method to send answers to callback queries sent from [inline
     * keyboards](https://core.telegram.org/bots/features#inline-keyboards). The answer will be
     * displayed to the user as a notification at the top of the chat screen or as an alert. On
     * success, *True* is returned.
     *
     * Alternatively, the user can be redirected to the specified Game URL. For this option to work,
     * you must first create a game for your bot via [@BotFather](https://t.me/botfather) and accept
     * the terms. Otherwise, you may use links like `t.me/your_bot?start=XXXX` that open your bot with
     * a parameter.
     *
     * @param callbackQueryId Unique identifier for the query to be answered
     * @param text Text of the notification. If not specified, nothing will be shown to the user,
     * 0-200 characters
     * @param showAlert If *True*, an alert will be shown by the client instead of a notification at
     * the top of the chat screen. Defaults to *false*.
     * @param url URL that will be opened by the user's client. If you have created a
     * [Game](https://core.telegram.org/bots/api/#game) and accepted the conditions via
     * [@BotFather](https://t.me/botfather), specify the URL that opens your game - note that this will
     * only work if the query comes from a
     * [*callback_game*](https://core.telegram.org/bots/api/#inlinekeyboardbutton) button.  
     *
     * Otherwise, you may use links like `t.me/your_bot?start=XXXX` that open your bot with a
     * parameter.
     * @param cacheTime The maximum amount of time in seconds that the result of the callback query
     * may be cached client-side. Telegram apps will support caching starting in version 3.14. Defaults
     * to 0.
     */
    public suspend fun answerCallbackQuery(
        callbackQueryId: String,
        text: String? = null,
        showAlert: Boolean? = null,
        url: String? = null,
        cacheTime: Int? = null,
    ): Boolean

    /**
     * Use this method to get the list of boosts added to a chat by a user. Requires administrator
     * rights in the chat. Returns a
     * [UserChatBoosts](https://core.telegram.org/bots/api/#userchatboosts) object.
     *
     * @param chatId Unique identifier for the chat or username of the channel (in the format
     * `@channelusername`)
     * @param userId Unique identifier of the target user
     */
    public suspend fun getUserChatBoosts(chatId: String, userId: Long): UserChatBoosts

    /**
     * Use this method to get information about the connection of the bot with a business account.
     * Returns a [BusinessConnection](https://core.telegram.org/bots/api/#businessconnection) object on
     * success.
     *
     * @param businessConnectionId Unique identifier of the business connection
     */
    public suspend fun getBusinessConnection(businessConnectionId: String): BusinessConnection

    /**
     * Use this method to change the list of the bot's commands. See [this
     * manual](https://core.telegram.org/bots/features#commands) for more details about bot commands.
     * Returns *True* on success.
     *
     * @param commands A JSON-serialized list of bot commands to be set as the list of the bot's
     * commands. At most 100 commands can be specified.
     * @param scope A JSON-serialized object, describing scope of users for which the commands are
     * relevant. Defaults to
     * [BotCommandScopeDefault](https://core.telegram.org/bots/api/#botcommandscopedefault).
     * @param languageCode A two-letter ISO 639-1 language code. If empty, commands will be applied
     * to all users from the given scope, for whose language there are no dedicated commands
     */
    public suspend fun setMyCommands(
        commands: Iterable<BotCommand>,
        scope: BotCommandScope? = null,
        languageCode: String? = null,
    ): Boolean

    /**
     * Use this method to delete the list of the bot's commands for the given scope and user
     * language. After deletion, [higher level
     * commands](https://core.telegram.org/bots/api/#determining-list-of-commands) will be shown to
     * affected users. Returns *True* on success.
     *
     * @param scope A JSON-serialized object, describing scope of users for which the commands are
     * relevant. Defaults to
     * [BotCommandScopeDefault](https://core.telegram.org/bots/api/#botcommandscopedefault).
     * @param languageCode A two-letter ISO 639-1 language code. If empty, commands will be applied
     * to all users from the given scope, for whose language there are no dedicated commands
     */
    public suspend fun deleteMyCommands(scope: BotCommandScope? = null, languageCode: String? =
            null): Boolean

    /**
     * Use this method to get the current list of the bot's commands for the given scope and user
     * language. Returns an Array of [BotCommand](https://core.telegram.org/bots/api/#botcommand)
     * objects. If commands aren't set, an empty list is returned.
     *
     * @param scope A JSON-serialized object, describing scope of users. Defaults to
     * [BotCommandScopeDefault](https://core.telegram.org/bots/api/#botcommandscopedefault).
     * @param languageCode A two-letter ISO 639-1 language code or an empty string
     */
    public suspend fun getMyCommands(scope: BotCommandScope? = null, languageCode: String? = null):
            List<BotCommand>

    /**
     * Use this method to change the bot's name. Returns *True* on success.
     *
     * @param name New bot name; 0-64 characters. Pass an empty string to remove the dedicated name
     * for the given language.
     * @param languageCode A two-letter ISO 639-1 language code. If empty, the name will be shown to
     * all users for whose language there is no dedicated name.
     */
    public suspend fun setMyName(name: String? = null, languageCode: String? = null): Boolean

    /**
     * Use this method to get the current bot name for the given user language. Returns
     * [BotName](https://core.telegram.org/bots/api/#botname) on success.
     *
     * @param languageCode A two-letter ISO 639-1 language code or an empty string
     */
    public suspend fun getMyName(languageCode: String? = null): BotName

    /**
     * Use this method to change the bot's description, which is shown in the chat with the bot if
     * the chat is empty. Returns *True* on success.
     *
     * @param description New bot description; 0-512 characters. Pass an empty string to remove the
     * dedicated description for the given language.
     * @param languageCode A two-letter ISO 639-1 language code. If empty, the description will be
     * applied to all users for whose language there is no dedicated description.
     */
    public suspend fun setMyDescription(description: String? = null, languageCode: String? = null):
            Boolean

    /**
     * Use this method to get the current bot description for the given user language. Returns
     * [BotDescription](https://core.telegram.org/bots/api/#botdescription) on success.
     *
     * @param languageCode A two-letter ISO 639-1 language code or an empty string
     */
    public suspend fun getMyDescription(languageCode: String? = null): BotDescription

    /**
     * Use this method to change the bot's short description, which is shown on the bot's profile
     * page and is sent together with the link when users share the bot. Returns *True* on success.
     *
     * @param shortDescription New short description for the bot; 0-120 characters. Pass an empty
     * string to remove the dedicated short description for the given language.
     * @param languageCode A two-letter ISO 639-1 language code. If empty, the short description
     * will be applied to all users for whose language there is no dedicated short description.
     */
    public suspend fun setMyShortDescription(shortDescription: String? = null, languageCode: String?
            = null): Boolean

    /**
     * Use this method to get the current bot short description for the given user language. Returns
     * [BotShortDescription](https://core.telegram.org/bots/api/#botshortdescription) on success.
     *
     * @param languageCode A two-letter ISO 639-1 language code or an empty string
     */
    public suspend fun getMyShortDescription(languageCode: String? = null): BotShortDescription

    /**
     * Use this method to change the bot's menu button in a private chat, or the default menu
     * button. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target private chat. If not specified, default bot's
     * menu button will be changed
     * @param menuButton A JSON-serialized object for the bot's new menu button. Defaults to
     * [MenuButtonDefault](https://core.telegram.org/bots/api/#menubuttondefault)
     */
    public suspend fun setChatMenuButton(chatId: Long? = null, menuButton: MenuButton? = null):
            Boolean

    /**
     * Use this method to get the current value of the bot's menu button in a private chat, or the
     * default menu button. Returns [MenuButton](https://core.telegram.org/bots/api/#menubutton) on
     * success.
     *
     * @param chatId Unique identifier for the target private chat. If not specified, default bot's
     * menu button will be returned
     */
    public suspend fun getChatMenuButton(chatId: Long? = null): MenuButton

    /**
     * Use this method to change the default administrator rights requested by the bot when it's
     * added as an administrator to groups or channels. These rights will be suggested to users, but
     * they are free to modify the list before adding the bot. Returns *True* on success.
     *
     * @param rights A JSON-serialized object describing new default administrator rights. If not
     * specified, the default administrator rights will be cleared.
     * @param forChannels Pass *True* to change the default administrator rights of the bot in
     * channels. Otherwise, the default administrator rights of the bot for groups and supergroups will
     * be changed.
     */
    public suspend fun setMyDefaultAdministratorRights(rights: ChatAdministratorRights? = null,
            forChannels: Boolean? = null): Boolean

    /**
     * Use this method to get the current default administrator rights of the bot. Returns
     * [ChatAdministratorRights](https://core.telegram.org/bots/api/#chatadministratorrights) on
     * success.
     *
     * @param forChannels Pass *True* to get default administrator rights of the bot in channels.
     * Otherwise, default administrator rights of the bot for groups and supergroups will be returned.
     */
    public suspend fun getMyDefaultAdministratorRights(forChannels: Boolean? = null):
            ChatAdministratorRights

    /**
     * Use this method to edit text and [game](https://core.telegram.org/bots/api/#games) messages.
     * On success, if the edited message is not an inline message, the edited
     * [Message](https://core.telegram.org/bots/api/#message) is returned, otherwise *True* is
     * returned. Note that business messages that were not sent by the bot and do not contain an inline
     * keyboard can only be edited within **48 hours** from the time they were sent.
     *
     * @param chatId Required if *inline_message_id* is not specified. Unique identifier for the
     * target chat or username of the target channel (in the format `@channelusername`)
     * @param messageId Required if *inline_message_id* is not specified. Identifier of the message
     * to edit
     * @param text New text of the message, 1-4096 characters after entities parsing
     * @param businessConnectionId Unique identifier of the business connection on behalf of which
     * the message to be edited was sent
     * @param parseMode Mode for parsing entities in the message text. See [formatting
     * options](https://core.telegram.org/bots/api/#formatting-options) for more details.
     * @param entities A JSON-serialized list of special entities that appear in message text, which
     * can be specified instead of *parse_mode*
     * @param linkPreviewOptions Link preview generation options for the message
     * @param replyMarkup A JSON-serialized object for an [inline
     * keyboard](https://core.telegram.org/bots/features#inline-keyboards).
     */
    public suspend fun editMessageText(
        chatId: String,
        messageId: Long,
        text: String,
        businessConnectionId: String? = null,
        parseMode: String? = null,
        entities: Iterable<MessageEntity>? = null,
        linkPreviewOptions: LinkPreviewOptions? = null,
        replyMarkup: InlineKeyboardMarkup? = null,
    ): Message

    /**
     * Use this method to edit captions of messages. On success, if the edited message is not an
     * inline message, the edited [Message](https://core.telegram.org/bots/api/#message) is returned,
     * otherwise *True* is returned. Note that business messages that were not sent by the bot and do
     * not contain an inline keyboard can only be edited within **48 hours** from the time they were
     * sent.
     *
     * @param chatId Required if *inline_message_id* is not specified. Unique identifier for the
     * target chat or username of the target channel (in the format `@channelusername`)
     * @param messageId Required if *inline_message_id* is not specified. Identifier of the message
     * to edit
     * @param businessConnectionId Unique identifier of the business connection on behalf of which
     * the message to be edited was sent
     * @param caption New caption of the message, 0-1024 characters after entities parsing
     * @param parseMode Mode for parsing entities in the message caption. See [formatting
     * options](https://core.telegram.org/bots/api/#formatting-options) for more details.
     * @param captionEntities A JSON-serialized list of special entities that appear in the caption,
     * which can be specified instead of *parse_mode*
     * @param showCaptionAboveMedia Pass *True*, if the caption must be shown above the message
     * media. Supported only for animation, photo and video messages.
     * @param replyMarkup A JSON-serialized object for an [inline
     * keyboard](https://core.telegram.org/bots/features#inline-keyboards).
     */
    public suspend fun editMessageCaption(
        chatId: String,
        messageId: Long,
        businessConnectionId: String? = null,
        caption: String? = null,
        parseMode: String? = null,
        captionEntities: Iterable<MessageEntity>? = null,
        showCaptionAboveMedia: Boolean? = null,
        replyMarkup: InlineKeyboardMarkup? = null,
    ): Message

    /**
     * Use this method to edit animation, audio, document, photo, or video messages. If a message is
     * part of a message album, then it can be edited only to an audio for audio albums, only to a
     * document for document albums and to a photo or a video otherwise. When an inline message is
     * edited, a new file can't be uploaded; use a previously uploaded file via its file_id or specify
     * a URL. On success, if the edited message is not an inline message, the edited
     * [Message](https://core.telegram.org/bots/api/#message) is returned, otherwise *True* is
     * returned. Note that business messages that were not sent by the bot and do not contain an inline
     * keyboard can only be edited within **48 hours** from the time they were sent.
     *
     * @param chatId Required if *inline_message_id* is not specified. Unique identifier for the
     * target chat or username of the target channel (in the format `@channelusername`)
     * @param messageId Required if *inline_message_id* is not specified. Identifier of the message
     * to edit
     * @param media A JSON-serialized object for a new media content of the message
     * @param businessConnectionId Unique identifier of the business connection on behalf of which
     * the message to be edited was sent
     * @param replyMarkup A JSON-serialized object for a new [inline
     * keyboard](https://core.telegram.org/bots/features#inline-keyboards).
     */
    public suspend fun editMessageMedia(
        chatId: String,
        messageId: Long,
        media: InputMedia,
        businessConnectionId: String? = null,
        replyMarkup: InlineKeyboardMarkup? = null,
    ): Message

    /**
     * Use this method to edit live location messages. A location can be edited until its
     * *live_period* expires or editing is explicitly disabled by a call to
     * [stopMessageLiveLocation](https://core.telegram.org/bots/api/#stopmessagelivelocation). On
     * success, if the edited message is not an inline message, the edited
     * [Message](https://core.telegram.org/bots/api/#message) is returned, otherwise *True* is
     * returned.
     *
     * @param chatId Required if *inline_message_id* is not specified. Unique identifier for the
     * target chat or username of the target channel (in the format `@channelusername`)
     * @param messageId Required if *inline_message_id* is not specified. Identifier of the message
     * to edit
     * @param latitude Latitude of new location
     * @param longitude Longitude of new location
     * @param businessConnectionId Unique identifier of the business connection on behalf of which
     * the message to be edited was sent
     * @param livePeriod New period in seconds during which the location can be updated, starting
     * from the message send date. If 0x7FFFFFFF is specified, then the location can be updated
     * forever. Otherwise, the new value must not exceed the current *live_period* by more than a day,
     * and the live location expiration date must remain within the next 90 days. If not specified,
     * then *live_period* remains unchanged
     * @param horizontalAccuracy The radius of uncertainty for the location, measured in meters;
     * 0-1500
     * @param heading Direction in which the user is moving, in degrees. Must be between 1 and 360
     * if specified.
     * @param proximityAlertRadius The maximum distance for proximity alerts about approaching
     * another chat member, in meters. Must be between 1 and 100000 if specified.
     * @param replyMarkup A JSON-serialized object for a new [inline
     * keyboard](https://core.telegram.org/bots/features#inline-keyboards).
     */
    public suspend fun editMessageLiveLocation(
        chatId: String,
        messageId: Long,
        latitude: Double,
        longitude: Double,
        businessConnectionId: String? = null,
        livePeriod: Int? = null,
        horizontalAccuracy: Double? = null,
        heading: Int? = null,
        proximityAlertRadius: Int? = null,
        replyMarkup: InlineKeyboardMarkup? = null,
    ): Message

    /**
     * Use this method to stop updating a live location message before *live_period* expires. On
     * success, if the message is not an inline message, the edited
     * [Message](https://core.telegram.org/bots/api/#message) is returned, otherwise *True* is
     * returned.
     *
     * @param chatId Required if *inline_message_id* is not specified. Unique identifier for the
     * target chat or username of the target channel (in the format `@channelusername`)
     * @param messageId Required if *inline_message_id* is not specified. Identifier of the message
     * with live location to stop
     * @param businessConnectionId Unique identifier of the business connection on behalf of which
     * the message to be edited was sent
     * @param replyMarkup A JSON-serialized object for a new [inline
     * keyboard](https://core.telegram.org/bots/features#inline-keyboards).
     */
    public suspend fun stopMessageLiveLocation(
        chatId: String,
        messageId: Long,
        businessConnectionId: String? = null,
        replyMarkup: InlineKeyboardMarkup? = null,
    ): Message

    /**
     * Use this method to edit only the reply markup of messages. On success, if the edited message
     * is not an inline message, the edited [Message](https://core.telegram.org/bots/api/#message) is
     * returned, otherwise *True* is returned. Note that business messages that were not sent by the
     * bot and do not contain an inline keyboard can only be edited within **48 hours** from the time
     * they were sent.
     *
     * @param chatId Required if *inline_message_id* is not specified. Unique identifier for the
     * target chat or username of the target channel (in the format `@channelusername`)
     * @param messageId Required if *inline_message_id* is not specified. Identifier of the message
     * to edit
     * @param businessConnectionId Unique identifier of the business connection on behalf of which
     * the message to be edited was sent
     * @param replyMarkup A JSON-serialized object for an [inline
     * keyboard](https://core.telegram.org/bots/features#inline-keyboards).
     */
    public suspend fun editMessageReplyMarkup(
        chatId: String,
        messageId: Long,
        businessConnectionId: String? = null,
        replyMarkup: InlineKeyboardMarkup? = null,
    ): Message

    /**
     * Use this method to stop a poll which was sent by the bot. On success, the stopped
     * [Poll](https://core.telegram.org/bots/api/#poll) is returned.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the
     * format `@channelusername`)
     * @param messageId Identifier of the original message with the poll
     * @param businessConnectionId Unique identifier of the business connection on behalf of which
     * the message to be edited was sent
     * @param replyMarkup A JSON-serialized object for a new message [inline
     * keyboard](https://core.telegram.org/bots/features#inline-keyboards).
     */
    public suspend fun stopPoll(
        chatId: String,
        messageId: Long,
        businessConnectionId: String? = null,
        replyMarkup: InlineKeyboardMarkup? = null,
    ): Poll

    /**
     * Use this method to delete a message, including service messages, with the following
     * limitations:  
     * - A message can only be deleted if it was sent less than 48 hours ago.  
     * - Service messages about a supergroup, channel, or forum topic creation can't be deleted.  
     * - A dice message in a private chat can only be deleted if it was sent more than 24 hours ago.
     *  
     * - Bots can delete outgoing messages in private chats, groups, and supergroups.  
     * - Bots can delete incoming messages in private chats.  
     * - Bots granted *can_post_messages* permissions can delete outgoing messages in channels.  
     * - If the bot is an administrator of a group, it can delete any message there.  
     * - If the bot has *can_delete_messages* permission in a supergroup or a channel, it can delete
     * any message there.  
     * Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the
     * format `@channelusername`)
     * @param messageId Identifier of the message to delete
     */
    public suspend fun deleteMessage(chatId: String, messageId: Long): Boolean

    /**
     * Use this method to delete multiple messages simultaneously. If some of the specified messages
     * can't be found, they are skipped. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the
     * format `@channelusername`)
     * @param messageIds A JSON-serialized list of 1-100 identifiers of messages to delete. See
     * [deleteMessage](https://core.telegram.org/bots/api/#deletemessage) for limitations on which
     * messages can be deleted
     */
    public suspend fun deleteMessages(chatId: String, messageIds: Iterable<Long>): Boolean

    /**
     * Use this method to send static .WEBP, [animated](https://telegram.org/blog/animated-stickers)
     * .TGS, or [video](https://telegram.org/blog/video-stickers-better-reactions) .WEBM stickers. On
     * success, the sent [Message](https://core.telegram.org/bots/api/#message) is returned.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the
     * format `@channelusername`)
     * @param sticker Sticker to send. Pass a file_id as String to send a file that exists on the
     * Telegram servers (recommended), pass an HTTP URL as a String for Telegram to get a .WEBP sticker
     * from the Internet, or upload a new .WEBP, .TGS, or .WEBM sticker using multipart/form-data.
     * [More information on Sending Files ](https://core.telegram.org/bots/api/#sending-files). Video
     * and animated stickers can't be sent via an HTTP URL.
     * @param businessConnectionId Unique identifier of the business connection on behalf of which
     * the message will be sent
     * @param messageThreadId Unique identifier for the target message thread (topic) of the forum;
     * for forum supergroups only
     * @param emoji Emoji associated with the sticker; only for just uploaded stickers
     * @param disableNotification Sends the message
     * [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a
     * notification with no sound.
     * @param protectContent Protects the contents of the sent message from forwarding and saving
     * @param messageEffectId Unique identifier of the message effect to be added to the message;
     * for private chats only
     * @param replyParameters Description of the message to reply to
     * @param replyMarkup Additional interface options. A JSON-serialized object for an [inline
     * keyboard](https://core.telegram.org/bots/features#inline-keyboards), [custom reply
     * keyboard](https://core.telegram.org/bots/features#keyboards), instructions to remove a reply
     * keyboard or to force a reply from the user
     */
    public suspend fun sendSticker(
        chatId: String,
        sticker: Input,
        businessConnectionId: String? = null,
        messageThreadId: Long? = null,
        emoji: String? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        messageEffectId: String? = null,
        replyParameters: ReplyParameters? = null,
        replyMarkup: ReplyMarkup? = null,
    ): Message

    /**
     * Use this method to get a sticker set. On success, a
     * [StickerSet](https://core.telegram.org/bots/api/#stickerset) object is returned.
     *
     * @param name Name of the sticker set
     */
    public suspend fun getStickerSet(name: String): StickerSet

    /**
     * Use this method to get information about custom emoji stickers by their identifiers. Returns
     * an Array of [Sticker](https://core.telegram.org/bots/api/#sticker) objects.
     *
     * @param customEmojiIds A JSON-serialized list of custom emoji identifiers. At most 200 custom
     * emoji identifiers can be specified.
     */
    public suspend fun getCustomEmojiStickers(customEmojiIds: Iterable<String>): List<Sticker>

    /**
     * Use this method to upload a file with a sticker for later use in the
     * [createNewStickerSet](https://core.telegram.org/bots/api/#createnewstickerset),
     * [addStickerToSet](https://core.telegram.org/bots/api/#addstickertoset), or
     * [replaceStickerInSet](https://core.telegram.org/bots/api/#replacestickerinset) methods (the file
     * can be used multiple times). Returns the uploaded
     * [File](https://core.telegram.org/bots/api/#file) on success.
     *
     * @param userId User identifier of sticker file owner
     * @param sticker A file with the sticker in .WEBP, .PNG, .TGS, or .WEBM format. See
     * [https://core.telegram.org/stickers](https://core.telegram.org/stickers) for technical
     * requirements. [More information on Sending Files
     * ](https://core.telegram.org/bots/api/#sending-files)
     * @param stickerFormat Format of the sticker, must be one of “static”, “animated”, “video”
     */
    public suspend fun uploadStickerFile(
        userId: Long,
        sticker: Input,
        stickerFormat: String,
    ): File

    /**
     * Use this method to create a new sticker set owned by a user. The bot will be able to edit the
     * sticker set thus created. Returns *True* on success.
     *
     * @param userId User identifier of created sticker set owner
     * @param name Short name of sticker set, to be used in `t.me/addstickers/` URLs (e.g.,
     * *animals*). Can contain only English letters, digits and underscores. Must begin with a letter,
     * can't contain consecutive underscores and must end in `"_by_<bot_username>"`. `<bot_username>`
     * is case insensitive. 1-64 characters.
     * @param title Sticker set title, 1-64 characters
     * @param stickers A JSON-serialized list of 1-50 initial stickers to be added to the sticker
     * set
     * @param stickerType Type of stickers in the set, pass “regular”, “mask”, or “custom_emoji”. By
     * default, a regular sticker set is created.
     * @param needsRepainting Pass *True* if stickers in the sticker set must be repainted to the
     * color of text when used in messages, the accent color if used as emoji status, white on chat
     * photos, or another appropriate color based on context; for custom emoji sticker sets only
     */
    public suspend fun createNewStickerSet(
        userId: Long,
        name: String,
        title: String,
        stickers: Iterable<InputSticker>,
        stickerType: String? = null,
        needsRepainting: Boolean? = null,
    ): Boolean

    /**
     * Use this method to add a new sticker to a set created by the bot. Emoji sticker sets can have
     * up to 200 stickers. Other sticker sets can have up to 120 stickers. Returns *True* on success.
     *
     * @param userId User identifier of sticker set owner
     * @param name Sticker set name
     * @param sticker A JSON-serialized object with information about the added sticker. If exactly
     * the same sticker had already been added to the set, then the set isn't changed.
     */
    public suspend fun addStickerToSet(
        userId: Long,
        name: String,
        sticker: InputSticker,
    ): Boolean

    /**
     * Use this method to move a sticker in a set created by the bot to a specific position. Returns
     * *True* on success.
     *
     * @param sticker File identifier of the sticker
     * @param position New sticker position in the set, zero-based
     */
    public suspend fun setStickerPositionInSet(sticker: String, position: Int): Boolean

    /**
     * Use this method to delete a sticker from a set created by the bot. Returns *True* on success.
     *
     * @param sticker File identifier of the sticker
     */
    public suspend fun deleteStickerFromSet(sticker: String): Boolean

    /**
     * Use this method to replace an existing sticker in a sticker set with a new one. The method is
     * equivalent to calling
     * [deleteStickerFromSet](https://core.telegram.org/bots/api/#deletestickerfromset), then
     * [addStickerToSet](https://core.telegram.org/bots/api/#addstickertoset), then
     * [setStickerPositionInSet](https://core.telegram.org/bots/api/#setstickerpositioninset). Returns
     * *True* on success.
     *
     * @param userId User identifier of the sticker set owner
     * @param name Sticker set name
     * @param oldSticker File identifier of the replaced sticker
     * @param sticker A JSON-serialized object with information about the added sticker. If exactly
     * the same sticker had already been added to the set, then the set remains unchanged.
     */
    public suspend fun replaceStickerInSet(
        userId: Long,
        name: String,
        oldSticker: String,
        sticker: InputSticker,
    ): Boolean

    /**
     * Use this method to change the list of emoji assigned to a regular or custom emoji sticker.
     * The sticker must belong to a sticker set created by the bot. Returns *True* on success.
     *
     * @param sticker File identifier of the sticker
     * @param emojiList A JSON-serialized list of 1-20 emoji associated with the sticker
     */
    public suspend fun setStickerEmojiList(sticker: String, emojiList: Iterable<String>): Boolean

    /**
     * Use this method to change search keywords assigned to a regular or custom emoji sticker. The
     * sticker must belong to a sticker set created by the bot. Returns *True* on success.
     *
     * @param sticker File identifier of the sticker
     * @param keywords A JSON-serialized list of 0-20 search keywords for the sticker with total
     * length of up to 64 characters
     */
    public suspend fun setStickerKeywords(sticker: String, keywords: Iterable<String>? = null):
            Boolean

    /**
     * Use this method to change the [mask
     * position](https://core.telegram.org/bots/api/#maskposition) of a mask sticker. The sticker must
     * belong to a sticker set that was created by the bot. Returns *True* on success.
     *
     * @param sticker File identifier of the sticker
     * @param maskPosition A JSON-serialized object with the position where the mask should be
     * placed on faces. Omit the parameter to remove the mask position.
     */
    public suspend fun setStickerMaskPosition(sticker: String, maskPosition: MaskPosition? = null):
            Boolean

    /**
     * Use this method to set the title of a created sticker set. Returns *True* on success.
     *
     * @param name Sticker set name
     * @param title Sticker set title, 1-64 characters
     */
    public suspend fun setStickerSetTitle(name: String, title: String): Boolean

    /**
     * Use this method to set the thumbnail of a regular or mask sticker set. The format of the
     * thumbnail file must match the format of the stickers in the set. Returns *True* on success.
     *
     * @param name Sticker set name
     * @param userId User identifier of the sticker set owner
     * @param format Format of the thumbnail, must be one of “static” for a **.WEBP** or **.PNG**
     * image, “animated” for a **.TGS** animation, or “video” for a **WEBM** video
     * @param thumbnail A **.WEBP** or **.PNG** image with the thumbnail, must be up to 128
     * kilobytes in size and have a width and height of exactly 100px, or a **.TGS** animation with a
     * thumbnail up to 32 kilobytes in size (see
     * [https://core.telegram.org/stickers#animated-sticker-requirements](https://core.telegram.org/stickers#animated-sticker-requirements)
     * for animated sticker technical requirements), or a **WEBM** video with the thumbnail up to 32
     * kilobytes in size; see
     * [https://core.telegram.org/stickers#video-sticker-requirements](https://core.telegram.org/stickers#video-sticker-requirements)
     * for video sticker technical requirements. Pass a *file_id* as a String to send a file that
     * already exists on the Telegram servers, pass an HTTP URL as a String for Telegram to get a file
     * from the Internet, or upload a new one using multipart/form-data. [More information on Sending
     * Files ](https://core.telegram.org/bots/api/#sending-files). Animated and video sticker set
     * thumbnails can't be uploaded via HTTP URL. If omitted, then the thumbnail is dropped and the
     * first sticker is used as the thumbnail.
     */
    public suspend fun setStickerSetThumbnail(
        name: String,
        userId: Long,
        format: String,
        thumbnail: Input? = null,
    ): Boolean

    /**
     * Use this method to set the thumbnail of a custom emoji sticker set. Returns *True* on
     * success.
     *
     * @param name Sticker set name
     * @param customEmojiId Custom emoji identifier of a sticker from the sticker set; pass an empty
     * string to drop the thumbnail and use the first sticker as the thumbnail.
     */
    public suspend fun setCustomEmojiStickerSetThumbnail(name: String, customEmojiId: String? =
            null): Boolean

    /**
     * Use this method to delete a sticker set that was created by the bot. Returns *True* on
     * success.
     *
     * @param name Sticker set name
     */
    public suspend fun deleteStickerSet(name: String): Boolean

    /**
     * Use this method to send answers to an inline query. On success, *True* is returned.  
     * No more than **50** results per query are allowed.
     *
     * @param inlineQueryId Unique identifier for the answered query
     * @param results A JSON-serialized array of results for the inline query
     * @param cacheTime The maximum amount of time in seconds that the result of the inline query
     * may be cached on the server. Defaults to 300.
     * @param isPersonal Pass *True* if results may be cached on the server side only for the user
     * that sent the query. By default, results may be returned to any user who sends the same query.
     * @param nextOffset Pass the offset that a client should send in the next query with the same
     * text to receive more results. Pass an empty string if there are no more results or if you don't
     * support pagination. Offset length can't exceed 64 bytes.
     * @param button A JSON-serialized object describing a button to be shown above inline query
     * results
     */
    public suspend fun answerInlineQuery(
        inlineQueryId: String,
        results: Iterable<InlineQueryResult>,
        cacheTime: Int? = null,
        isPersonal: Boolean? = null,
        nextOffset: String? = null,
        button: InlineQueryResultsButton? = null,
    ): Boolean

    /**
     * Use this method to set the result of an interaction with a [Web
     * App](https://core.telegram.org/bots/webapps) and send a corresponding message on behalf of the
     * user to the chat from which the query originated. On success, a
     * [SentWebAppMessage](https://core.telegram.org/bots/api/#sentwebappmessage) object is returned.
     *
     * @param webAppQueryId Unique identifier for the query to be answered
     * @param result A JSON-serialized object describing the message to be sent
     */
    public suspend fun answerWebAppQuery(webAppQueryId: String, result: InlineQueryResult):
            SentWebAppMessage

    /**
     * Use this method to send invoices. On success, the sent
     * [Message](https://core.telegram.org/bots/api/#message) is returned.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the
     * format `@channelusername`)
     * @param title Product name, 1-32 characters
     * @param description Product description, 1-255 characters
     * @param payload Bot-defined invoice payload, 1-128 bytes. This will not be displayed to the
     * user, use for your internal processes.
     * @param currency Three-letter ISO 4217 currency code, see [more on
     * currencies](https://core.telegram.org/bots/payments#supported-currencies). Pass “XTR” for
     * payments in [Telegram Stars](https://t.me/BotNews/90).
     * @param prices Price breakdown, a JSON-serialized list of components (e.g. product price, tax,
     * discount, delivery cost, delivery tax, bonus, etc.). Must contain exactly one item for payments
     * in [Telegram Stars](https://t.me/BotNews/90).
     * @param messageThreadId Unique identifier for the target message thread (topic) of the forum;
     * for forum supergroups only
     * @param providerToken Payment provider token, obtained via
     * [@BotFather](https://t.me/botfather). Pass an empty string for payments in [Telegram
     * Stars](https://t.me/BotNews/90).
     * @param maxTipAmount The maximum accepted amount for tips in the *smallest units* of the
     * currency (integer, **not** float/double). For example, for a maximum tip of `US$ 1.45` pass
     * `max_tip_amount = 145`. See the *exp* parameter in
     * [currencies.json](https://core.telegram.org/bots/payments/currencies.json), it shows the number
     * of digits past the decimal point for each currency (2 for the majority of currencies). Defaults
     * to 0. Not supported for payments in [Telegram Stars](https://t.me/BotNews/90).
     * @param suggestedTipAmounts A JSON-serialized array of suggested amounts of tips in the
     * *smallest units* of the currency (integer, **not** float/double). At most 4 suggested tip
     * amounts can be specified. The suggested tip amounts must be positive, passed in a strictly
     * increased order and must not exceed *max_tip_amount*.
     * @param startParameter Unique deep-linking parameter. If left empty, **forwarded copies** of
     * the sent message will have a *Pay* button, allowing multiple users to pay directly from the
     * forwarded message, using the same invoice. If non-empty, forwarded copies of the sent message
     * will have a *URL* button with a deep link to the bot (instead of a *Pay* button), with the value
     * used as the start parameter
     * @param providerData JSON-serialized data about the invoice, which will be shared with the
     * payment provider. A detailed description of required fields should be provided by the payment
     * provider.
     * @param photoUrl URL of the product photo for the invoice. Can be a photo of the goods or a
     * marketing image for a service. People like it better when they see what they are paying for.
     * @param photoSize Photo size in bytes
     * @param photoWidth Photo width
     * @param photoHeight Photo height
     * @param needName Pass *True* if you require the user's full name to complete the order.
     * Ignored for payments in [Telegram Stars](https://t.me/BotNews/90).
     * @param needPhoneNumber Pass *True* if you require the user's phone number to complete the
     * order. Ignored for payments in [Telegram Stars](https://t.me/BotNews/90).
     * @param needEmail Pass *True* if you require the user's email address to complete the order.
     * Ignored for payments in [Telegram Stars](https://t.me/BotNews/90).
     * @param needShippingAddress Pass *True* if you require the user's shipping address to complete
     * the order. Ignored for payments in [Telegram Stars](https://t.me/BotNews/90).
     * @param sendPhoneNumberToProvider Pass *True* if the user's phone number should be sent to the
     * provider. Ignored for payments in [Telegram Stars](https://t.me/BotNews/90).
     * @param sendEmailToProvider Pass *True* if the user's email address should be sent to the
     * provider. Ignored for payments in [Telegram Stars](https://t.me/BotNews/90).
     * @param isFlexible Pass *True* if the final price depends on the shipping method. Ignored for
     * payments in [Telegram Stars](https://t.me/BotNews/90).
     * @param disableNotification Sends the message
     * [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a
     * notification with no sound.
     * @param protectContent Protects the contents of the sent message from forwarding and saving
     * @param messageEffectId Unique identifier of the message effect to be added to the message;
     * for private chats only
     * @param replyParameters Description of the message to reply to
     * @param replyMarkup A JSON-serialized object for an [inline
     * keyboard](https://core.telegram.org/bots/features#inline-keyboards). If empty, one 'Pay `total
     * price`' button will be shown. If not empty, the first button must be a Pay button.
     */
    public suspend fun sendInvoice(
        chatId: String,
        title: String,
        description: String,
        payload: String,
        currency: String,
        prices: Iterable<LabeledPrice>,
        messageThreadId: Long? = null,
        providerToken: String? = null,
        maxTipAmount: Int? = null,
        suggestedTipAmounts: Iterable<Int>? = null,
        startParameter: String? = null,
        providerData: String? = null,
        photoUrl: String? = null,
        photoSize: Int? = null,
        photoWidth: Int? = null,
        photoHeight: Int? = null,
        needName: Boolean? = null,
        needPhoneNumber: Boolean? = null,
        needEmail: Boolean? = null,
        needShippingAddress: Boolean? = null,
        sendPhoneNumberToProvider: Boolean? = null,
        sendEmailToProvider: Boolean? = null,
        isFlexible: Boolean? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        messageEffectId: String? = null,
        replyParameters: ReplyParameters? = null,
        replyMarkup: InlineKeyboardMarkup? = null,
    ): Message

    /**
     * Use this method to create a link for an invoice. Returns the created invoice link as *String*
     * on success.
     *
     * @param title Product name, 1-32 characters
     * @param description Product description, 1-255 characters
     * @param payload Bot-defined invoice payload, 1-128 bytes. This will not be displayed to the
     * user, use for your internal processes.
     * @param currency Three-letter ISO 4217 currency code, see [more on
     * currencies](https://core.telegram.org/bots/payments#supported-currencies). Pass “XTR” for
     * payments in [Telegram Stars](https://t.me/BotNews/90).
     * @param prices Price breakdown, a JSON-serialized list of components (e.g. product price, tax,
     * discount, delivery cost, delivery tax, bonus, etc.). Must contain exactly one item for payments
     * in [Telegram Stars](https://t.me/BotNews/90).
     * @param providerToken Payment provider token, obtained via
     * [@BotFather](https://t.me/botfather). Pass an empty string for payments in [Telegram
     * Stars](https://t.me/BotNews/90).
     * @param maxTipAmount The maximum accepted amount for tips in the *smallest units* of the
     * currency (integer, **not** float/double). For example, for a maximum tip of `US$ 1.45` pass
     * `max_tip_amount = 145`. See the *exp* parameter in
     * [currencies.json](https://core.telegram.org/bots/payments/currencies.json), it shows the number
     * of digits past the decimal point for each currency (2 for the majority of currencies). Defaults
     * to 0. Not supported for payments in [Telegram Stars](https://t.me/BotNews/90).
     * @param suggestedTipAmounts A JSON-serialized array of suggested amounts of tips in the
     * *smallest units* of the currency (integer, **not** float/double). At most 4 suggested tip
     * amounts can be specified. The suggested tip amounts must be positive, passed in a strictly
     * increased order and must not exceed *max_tip_amount*.
     * @param providerData JSON-serialized data about the invoice, which will be shared with the
     * payment provider. A detailed description of required fields should be provided by the payment
     * provider.
     * @param photoUrl URL of the product photo for the invoice. Can be a photo of the goods or a
     * marketing image for a service.
     * @param photoSize Photo size in bytes
     * @param photoWidth Photo width
     * @param photoHeight Photo height
     * @param needName Pass *True* if you require the user's full name to complete the order.
     * Ignored for payments in [Telegram Stars](https://t.me/BotNews/90).
     * @param needPhoneNumber Pass *True* if you require the user's phone number to complete the
     * order. Ignored for payments in [Telegram Stars](https://t.me/BotNews/90).
     * @param needEmail Pass *True* if you require the user's email address to complete the order.
     * Ignored for payments in [Telegram Stars](https://t.me/BotNews/90).
     * @param needShippingAddress Pass *True* if you require the user's shipping address to complete
     * the order. Ignored for payments in [Telegram Stars](https://t.me/BotNews/90).
     * @param sendPhoneNumberToProvider Pass *True* if the user's phone number should be sent to the
     * provider. Ignored for payments in [Telegram Stars](https://t.me/BotNews/90).
     * @param sendEmailToProvider Pass *True* if the user's email address should be sent to the
     * provider. Ignored for payments in [Telegram Stars](https://t.me/BotNews/90).
     * @param isFlexible Pass *True* if the final price depends on the shipping method. Ignored for
     * payments in [Telegram Stars](https://t.me/BotNews/90).
     */
    public suspend fun createInvoiceLink(
        title: String,
        description: String,
        payload: String,
        currency: String,
        prices: Iterable<LabeledPrice>,
        providerToken: String? = null,
        maxTipAmount: Int? = null,
        suggestedTipAmounts: Iterable<Int>? = null,
        providerData: String? = null,
        photoUrl: String? = null,
        photoSize: Int? = null,
        photoWidth: Int? = null,
        photoHeight: Int? = null,
        needName: Boolean? = null,
        needPhoneNumber: Boolean? = null,
        needEmail: Boolean? = null,
        needShippingAddress: Boolean? = null,
        sendPhoneNumberToProvider: Boolean? = null,
        sendEmailToProvider: Boolean? = null,
        isFlexible: Boolean? = null,
    ): String

    /**
     * If you sent an invoice requesting a shipping address and the parameter *is_flexible* was
     * specified, the Bot API will send an [Update](https://core.telegram.org/bots/api/#update) with a
     * *shipping_query* field to the bot. Use this method to reply to shipping queries. On success,
     * *True* is returned.
     *
     * @param shippingQueryId Unique identifier for the query to be answered
     * @param ok Pass *True* if delivery to the specified address is possible and *False* if there
     * are any problems (for example, if delivery to the specified address is not possible)
     * @param shippingOptions Required if *ok* is *True*. A JSON-serialized array of available
     * shipping options.
     * @param errorMessage Required if *ok* is *False*. Error message in human readable form that
     * explains why it is impossible to complete the order (e.g. "Sorry, delivery to your desired
     * address is unavailable'). Telegram will display this message to the user.
     */
    public suspend fun answerShippingQuery(
        shippingQueryId: String,
        ok: Boolean,
        shippingOptions: Iterable<ShippingOption>? = null,
        errorMessage: String? = null,
    ): Boolean

    /**
     * Once the user has confirmed their payment and shipping details, the Bot API sends the final
     * confirmation in the form of an [Update](https://core.telegram.org/bots/api/#update) with the
     * field *pre_checkout_query*. Use this method to respond to such pre-checkout queries. On success,
     * *True* is returned. **Note:** The Bot API must receive an answer within 10 seconds after the
     * pre-checkout query was sent.
     *
     * @param preCheckoutQueryId Unique identifier for the query to be answered
     * @param ok Specify *True* if everything is alright (goods are available, etc.) and the bot is
     * ready to proceed with the order. Use *False* if there are any problems.
     * @param errorMessage Required if *ok* is *False*. Error message in human readable form that
     * explains the reason for failure to proceed with the checkout (e.g. "Sorry, somebody just bought
     * the last of our amazing black T-shirts while you were busy filling out your payment details.
     * Please choose a different color or garment!"). Telegram will display this message to the user.
     */
    public suspend fun answerPreCheckoutQuery(
        preCheckoutQueryId: String,
        ok: Boolean,
        errorMessage: String? = null,
    ): Boolean

    /**
     * Returns the bot's Telegram Star transactions in chronological order. On success, returns a
     * [StarTransactions](https://core.telegram.org/bots/api/#startransactions) object.
     *
     * @param offset Number of transactions to skip in the response
     * @param limit The maximum number of transactions to be retrieved. Values between 1-100 are
     * accepted. Defaults to 100.
     */
    public suspend fun getStarTransactions(offset: Int? = null, limit: Int? = null):
            StarTransactions

    /**
     * Refunds a successful payment in [Telegram Stars](https://t.me/BotNews/90). Returns *True* on
     * success.
     *
     * @param userId Identifier of the user whose payment will be refunded
     * @param telegramPaymentChargeId Telegram payment identifier
     */
    public suspend fun refundStarPayment(userId: Long, telegramPaymentChargeId: String): Boolean

    /**
     * Informs a user that some of the Telegram Passport elements they provided contains errors. The
     * user will not be able to re-submit their Passport to you until the errors are fixed (the
     * contents of the field for which you returned the error must change). Returns *True* on success.
     *
     * Use this if the data submitted by the user doesn't satisfy the standards your service
     * requires for any reason. For example, if a birthday date seems invalid, a submitted document is
     * blurry, a scan shows evidence of tampering, etc. Supply some details in the error message to
     * make sure the user knows how to correct the issues.
     *
     * @param userId User identifier
     * @param errors A JSON-serialized array describing the errors
     */
    public suspend fun setPassportDataErrors(userId: Long, errors: Iterable<PassportElementError>):
            Boolean

    /**
     * Use this method to send a game. On success, the sent
     * [Message](https://core.telegram.org/bots/api/#message) is returned.
     *
     * @param chatId Unique identifier for the target chat
     * @param gameShortName Short name of the game, serves as the unique identifier for the game.
     * Set up your games via [@BotFather](https://t.me/botfather).
     * @param businessConnectionId Unique identifier of the business connection on behalf of which
     * the message will be sent
     * @param messageThreadId Unique identifier for the target message thread (topic) of the forum;
     * for forum supergroups only
     * @param disableNotification Sends the message
     * [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a
     * notification with no sound.
     * @param protectContent Protects the contents of the sent message from forwarding and saving
     * @param messageEffectId Unique identifier of the message effect to be added to the message;
     * for private chats only
     * @param replyParameters Description of the message to reply to
     * @param replyMarkup A JSON-serialized object for an [inline
     * keyboard](https://core.telegram.org/bots/features#inline-keyboards). If empty, one 'Play
     * game_title' button will be shown. If not empty, the first button must launch the game.
     */
    public suspend fun sendGame(
        chatId: Long,
        gameShortName: String,
        businessConnectionId: String? = null,
        messageThreadId: Long? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        messageEffectId: String? = null,
        replyParameters: ReplyParameters? = null,
        replyMarkup: InlineKeyboardMarkup? = null,
    ): Message

    /**
     * Use this method to set the score of the specified user in a game message. On success, if the
     * message is not an inline message, the [Message](https://core.telegram.org/bots/api/#message) is
     * returned, otherwise *True* is returned. Returns an error, if the new score is not greater than
     * the user's current score in the chat and *force* is *False*.
     *
     * @param userId User identifier
     * @param score New score, must be non-negative
     * @param chatId Required if *inline_message_id* is not specified. Unique identifier for the
     * target chat
     * @param messageId Required if *inline_message_id* is not specified. Identifier of the sent
     * message
     * @param force Pass *True* if the high score is allowed to decrease. This can be useful when
     * fixing mistakes or banning cheaters
     * @param disableEditMessage Pass *True* if the game message should not be automatically edited
     * to include the current scoreboard
     */
    public suspend fun setGameScore(
        userId: Long,
        score: Int,
        chatId: Long,
        messageId: Long,
        force: Boolean? = null,
        disableEditMessage: Boolean? = null,
    ): Message

    /**
     * Use this method to get data for high score tables. Will return the score of the specified
     * user and several of their neighbors in a game. Returns an Array of
     * [GameHighScore](https://core.telegram.org/bots/api/#gamehighscore) objects.
     *
     * This method will currently return scores for the target user, plus two of their closest
     * neighbors on each side. Will also return the top three users if the user and their neighbors are
     * not among them. Please note that this behavior is subject to change.
     *
     * @param userId Target user id
     * @param chatId Required if *inline_message_id* is not specified. Unique identifier for the
     * target chat
     * @param messageId Required if *inline_message_id* is not specified. Identifier of the sent
     * message
     * @param inlineMessageId Required if *chat_id* and *message_id* are not specified. Identifier
     * of the inline message
     */
    public suspend fun getGameHighScores(
        userId: Long,
        chatId: Long? = null,
        messageId: Long? = null,
        inlineMessageId: String? = null,
    ): List<GameHighScore>

    /**
     * Use this method to edit text and [game](https://core.telegram.org/bots/api/#games) messages.
     * On success, if the edited message is not an inline message, the edited
     * [Message](https://core.telegram.org/bots/api/#message) is returned, otherwise *True* is
     * returned. Note that business messages that were not sent by the bot and do not contain an inline
     * keyboard can only be edited within **48 hours** from the time they were sent.
     *
     * @param inlineMessageId Required if *chat_id* and *message_id* are not specified. Identifier
     * of the inline message
     * @param text New text of the message, 1-4096 characters after entities parsing
     * @param businessConnectionId Unique identifier of the business connection on behalf of which
     * the message to be edited was sent
     * @param parseMode Mode for parsing entities in the message text. See [formatting
     * options](https://core.telegram.org/bots/api/#formatting-options) for more details.
     * @param entities A JSON-serialized list of special entities that appear in message text, which
     * can be specified instead of *parse_mode*
     * @param linkPreviewOptions Link preview generation options for the message
     * @param replyMarkup A JSON-serialized object for an [inline
     * keyboard](https://core.telegram.org/bots/features#inline-keyboards).
     */
    public suspend fun editMessageText(
        inlineMessageId: String,
        text: String,
        businessConnectionId: String? = null,
        parseMode: String? = null,
        entities: Iterable<MessageEntity>? = null,
        linkPreviewOptions: LinkPreviewOptions? = null,
        replyMarkup: InlineKeyboardMarkup? = null,
    ): Boolean

    /**
     * Use this method to edit captions of messages. On success, if the edited message is not an
     * inline message, the edited [Message](https://core.telegram.org/bots/api/#message) is returned,
     * otherwise *True* is returned. Note that business messages that were not sent by the bot and do
     * not contain an inline keyboard can only be edited within **48 hours** from the time they were
     * sent.
     *
     * @param inlineMessageId Required if *chat_id* and *message_id* are not specified. Identifier
     * of the inline message
     * @param businessConnectionId Unique identifier of the business connection on behalf of which
     * the message to be edited was sent
     * @param caption New caption of the message, 0-1024 characters after entities parsing
     * @param parseMode Mode for parsing entities in the message caption. See [formatting
     * options](https://core.telegram.org/bots/api/#formatting-options) for more details.
     * @param captionEntities A JSON-serialized list of special entities that appear in the caption,
     * which can be specified instead of *parse_mode*
     * @param showCaptionAboveMedia Pass *True*, if the caption must be shown above the message
     * media. Supported only for animation, photo and video messages.
     * @param replyMarkup A JSON-serialized object for an [inline
     * keyboard](https://core.telegram.org/bots/features#inline-keyboards).
     */
    public suspend fun editMessageCaption(
        inlineMessageId: String,
        businessConnectionId: String? = null,
        caption: String? = null,
        parseMode: String? = null,
        captionEntities: Iterable<MessageEntity>? = null,
        showCaptionAboveMedia: Boolean? = null,
        replyMarkup: InlineKeyboardMarkup? = null,
    ): Boolean

    /**
     * Use this method to edit animation, audio, document, photo, or video messages. If a message is
     * part of a message album, then it can be edited only to an audio for audio albums, only to a
     * document for document albums and to a photo or a video otherwise. When an inline message is
     * edited, a new file can't be uploaded; use a previously uploaded file via its file_id or specify
     * a URL. On success, if the edited message is not an inline message, the edited
     * [Message](https://core.telegram.org/bots/api/#message) is returned, otherwise *True* is
     * returned. Note that business messages that were not sent by the bot and do not contain an inline
     * keyboard can only be edited within **48 hours** from the time they were sent.
     *
     * @param inlineMessageId Required if *chat_id* and *message_id* are not specified. Identifier
     * of the inline message
     * @param media A JSON-serialized object for a new media content of the message
     * @param businessConnectionId Unique identifier of the business connection on behalf of which
     * the message to be edited was sent
     * @param replyMarkup A JSON-serialized object for a new [inline
     * keyboard](https://core.telegram.org/bots/features#inline-keyboards).
     */
    public suspend fun editMessageMedia(
        inlineMessageId: String,
        media: InputMedia,
        businessConnectionId: String? = null,
        replyMarkup: InlineKeyboardMarkup? = null,
    ): Boolean

    /**
     * Use this method to edit live location messages. A location can be edited until its
     * *live_period* expires or editing is explicitly disabled by a call to
     * [stopMessageLiveLocation](https://core.telegram.org/bots/api/#stopmessagelivelocation). On
     * success, if the edited message is not an inline message, the edited
     * [Message](https://core.telegram.org/bots/api/#message) is returned, otherwise *True* is
     * returned.
     *
     * @param inlineMessageId Required if *chat_id* and *message_id* are not specified. Identifier
     * of the inline message
     * @param latitude Latitude of new location
     * @param longitude Longitude of new location
     * @param businessConnectionId Unique identifier of the business connection on behalf of which
     * the message to be edited was sent
     * @param livePeriod New period in seconds during which the location can be updated, starting
     * from the message send date. If 0x7FFFFFFF is specified, then the location can be updated
     * forever. Otherwise, the new value must not exceed the current *live_period* by more than a day,
     * and the live location expiration date must remain within the next 90 days. If not specified,
     * then *live_period* remains unchanged
     * @param horizontalAccuracy The radius of uncertainty for the location, measured in meters;
     * 0-1500
     * @param heading Direction in which the user is moving, in degrees. Must be between 1 and 360
     * if specified.
     * @param proximityAlertRadius The maximum distance for proximity alerts about approaching
     * another chat member, in meters. Must be between 1 and 100000 if specified.
     * @param replyMarkup A JSON-serialized object for a new [inline
     * keyboard](https://core.telegram.org/bots/features#inline-keyboards).
     */
    public suspend fun editMessageLiveLocation(
        inlineMessageId: String,
        latitude: Double,
        longitude: Double,
        businessConnectionId: String? = null,
        livePeriod: Int? = null,
        horizontalAccuracy: Double? = null,
        heading: Int? = null,
        proximityAlertRadius: Int? = null,
        replyMarkup: InlineKeyboardMarkup? = null,
    ): Boolean

    /**
     * Use this method to stop updating a live location message before *live_period* expires. On
     * success, if the message is not an inline message, the edited
     * [Message](https://core.telegram.org/bots/api/#message) is returned, otherwise *True* is
     * returned.
     *
     * @param inlineMessageId Required if *chat_id* and *message_id* are not specified. Identifier
     * of the inline message
     * @param businessConnectionId Unique identifier of the business connection on behalf of which
     * the message to be edited was sent
     * @param replyMarkup A JSON-serialized object for a new [inline
     * keyboard](https://core.telegram.org/bots/features#inline-keyboards).
     */
    public suspend fun stopMessageLiveLocation(
        inlineMessageId: String,
        businessConnectionId: String? = null,
        replyMarkup: InlineKeyboardMarkup? = null,
    ): Boolean

    /**
     * Use this method to edit only the reply markup of messages. On success, if the edited message
     * is not an inline message, the edited [Message](https://core.telegram.org/bots/api/#message) is
     * returned, otherwise *True* is returned. Note that business messages that were not sent by the
     * bot and do not contain an inline keyboard can only be edited within **48 hours** from the time
     * they were sent.
     *
     * @param inlineMessageId Required if *chat_id* and *message_id* are not specified. Identifier
     * of the inline message
     * @param businessConnectionId Unique identifier of the business connection on behalf of which
     * the message to be edited was sent
     * @param replyMarkup A JSON-serialized object for an [inline
     * keyboard](https://core.telegram.org/bots/features#inline-keyboards).
     */
    public suspend fun editMessageReplyMarkup(
        inlineMessageId: String,
        businessConnectionId: String? = null,
        replyMarkup: InlineKeyboardMarkup? = null,
    ): Boolean

    /**
     * Use this method to set the score of the specified user in a game message. On success, if the
     * message is not an inline message, the [Message](https://core.telegram.org/bots/api/#message) is
     * returned, otherwise *True* is returned. Returns an error, if the new score is not greater than
     * the user's current score in the chat and *force* is *False*.
     *
     * @param userId User identifier
     * @param score New score, must be non-negative
     * @param inlineMessageId Required if *chat_id* and *message_id* are not specified. Identifier
     * of the inline message
     * @param force Pass *True* if the high score is allowed to decrease. This can be useful when
     * fixing mistakes or banning cheaters
     * @param disableEditMessage Pass *True* if the game message should not be automatically edited
     * to include the current scoreboard
     */
    public suspend fun setGameScore(
        userId: Long,
        score: Int,
        inlineMessageId: String,
        force: Boolean? = null,
        disableEditMessage: Boolean? = null,
    ): Boolean
}
