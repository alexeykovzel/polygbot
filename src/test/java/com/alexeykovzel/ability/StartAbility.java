package com.alexeykovzel.ability;

import com.alexeykovzel.commandRegistry.command.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StartAbility extends BotCommand {
    HelpAbility helpCommand;

    public StartAbility(HelpAbility helpCommand) {
        super("start", "this command starts the bot");
        this.helpCommand = helpCommand;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        int chatId = Math.toIntExact(chat.getId());

        helpCommand.execute(absSender, user, chat, new String[]{});
    }

    private InlineKeyboardMarkup getInlineKeyboardMarkup() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowList = Arrays.asList(
                Arrays.asList(
                        createButton("Excellent", "1"),
                        createButton("Good", "2")
                ),
                Arrays.asList(
                        createButton("So-so", "3"),
                        createButton("Normal", "4")
                )
        );

        markup.setKeyboard(rowList);
        return markup;
    }

    private InlineKeyboardButton createButton(String text, String callBackData){
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callBackData);
        return button;
    }
}
