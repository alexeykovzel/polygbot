package com.alexeykovzel.handler;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface BotHandler {

    /**
     * escape markdown
     *
     * @param text text
     * @return text without markdown
     */
    String escapeMarkdown(String text);

    /**
     * handle update
     *
     * @param update update
     * @throws TelegramApiException TelegramApiException
     */
    void handleUpdate(Update update) throws TelegramApiException;

    /**
     * process invalid command update
     *
     * @param update update
     */
    void processInvalidCommandUpdate(Update update);

    /**
     * process message with text
     *
     * @param message message
     */
    void processTextMessage(Message message);

    /**
     * process message without text
     *
     * @param message message
     */
    void processNonTextMessage(Message message);

    /**
     * handle callback query
     *
     * @param update update
     */
    void handleCallBackQuery(Update update);

    /**
     * process non-command update
     *
     * @param update update
     */
    void processNonCommandUpdate(Update update);

    /**
     * send message with inlineKeyboardMarkup
     *
     * @param chatId               chat id
     * @param text                 the String that you want to send as a message.
     * @param inlineKeyboardMarkup attached inline keyboard markup to a message
     */
    void sendMsg(String chatId, String text, InlineKeyboardMarkup inlineKeyboardMarkup);

    /**
     * send message
     *
     * @param chatId chat id
     * @param text   the String that you want to send as a message.
     */
    void sendMsg(String chatId, String text);

    /**
     * delete message
     *
     * @param chatId    chatId
     * @param messageId messageId
     */
    void deleteMsg(String chatId, Integer messageId);

    /**
     * send AnswerCallbackQuery
     *
     * @param text          text
     * @param alert         alert
     * @param callbackquery callbackquery
     */
    void sendAnswerCallbackQuery(String text, boolean alert, CallbackQuery callbackquery);

    /**
     * Return username of this bot
     */
    String getBotUsername();

    /**
     * Return bot token to access Telegram API
     */
    String getBotToken();
}
