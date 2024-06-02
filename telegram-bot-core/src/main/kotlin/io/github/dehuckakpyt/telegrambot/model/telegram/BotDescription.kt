package io.github.dehuckakpyt.telegrambot.model.telegram

import com.fasterxml.jackson.`annotation`.JsonProperty
import kotlin.String

/**
 * Created on 02.06.2024.
 *
 * This object represents the bot's description.
 *
 * @see [BotDescription] (https://core.telegram.org/bots/api/#botdescription)
 *
 * @author KScript
 *
 * @param description The bot's description
 */
public data class BotDescription(
    /**
     * The bot's description
     */
    @get:JsonProperty("description")
    @param:JsonProperty("description")
    public val description: String,
)
