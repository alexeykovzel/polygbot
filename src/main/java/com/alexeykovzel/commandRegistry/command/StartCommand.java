package com.alexeykovzel.commandRegistry.command;

import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.List;

/**
 * This command starts the bot
 *
 * @author alexeykovzel
 */
public class StartCommand extends BotCommand {
    HelpCommand helpCommand;

    public StartCommand(HelpCommand helpCommand) {
        super("start", "this command starts the bot");
        this.helpCommand = helpCommand;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        int chatId = Math.toIntExact(chat.getId());

        /*try {
            if (!ChatHome.checkChatExistence(chatId)) {
                ChatHome.saveChat(chatId, chat.getFirstName(), chat.getLastName(), chat.getUserName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }*/

        helpCommand.execute(absSender, user, chat, new String[]{});

        /*SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(properties.getProperty("startText"));
        message.setReplyMarkup(getInlineKeyboardMarkup());
        message.setSticker(new InputFile(new File("/home/aliakseik/Projects/polygbot/src/main/resources/pictures/anime-girl-demon.webp")));

        try {
            absSender.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }*/
    }

    private InlineKeyboardMarkup getInlineKeyboardMarkup() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("Excellent!");
        button1.setCallbackData("Hello World!");
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("Good");
        button2.setCallbackData("Hello World!");
        row1.add(button1);
        row1.add(button2);
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton button3 = new InlineKeyboardButton();
        button3.setText("Normal");
        button3.setCallbackData("Hello World!");
        InlineKeyboardButton button4 = new InlineKeyboardButton();
        button4.setText("So-so");
        button4.setCallbackData("Hello World!");
        row2.add(button3);
        row2.add(button4);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(row1);
        rowList.add(row2);
        markup.setKeyboard(rowList);
        return markup;
    }
}
