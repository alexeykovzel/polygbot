package com.alexeykovzel.bot.handlers;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface BotHandler {

    String escapeMarkdown(String text);

    void handleUpdate(Update update) throws TelegramApiException;

    void processInvalidCommandUpdate(Update update);

    void processTextMessage(Message message);

    void processNonTextMessage(Message message);

    void handleCallBackQuery(Update update);

    void processNonCommandUpdate(Update update);

    void sendMsg(String chatId, String text, InlineKeyboardMarkup inlineKeyboardMarkup);

    void sendMsg(String chatId, String text);

    void deleteMsg(String chatId, Integer messageId);

    void sendAnswerCallbackQuery(String text, boolean alert, CallbackQuery callbackquery);

    InlineKeyboardButton createInlineKeyboardButton(String text, String callbackData);

    String getBotUsername();
}
