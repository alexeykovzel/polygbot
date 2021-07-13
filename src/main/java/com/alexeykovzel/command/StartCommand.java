package com.alexeykovzel.command;

import com.alexeykovzel.database.entity.Chat;
import com.alexeykovzel.database.entity.User;
import com.alexeykovzel.database.repository.ChatRepository;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

/**
 * This command starts the bot
 *
 * @author alexeykovzel
 */
public class StartCommand extends BotCommand {
    private static final String COMMAND_IDENTIFIER = "start";
    private static final String COMMAND_DESCRIPTION = "this command starts the bot";
    private final ChatRepository chatRepository;
    HelpCommand helpCommand;


    public StartCommand(HelpCommand helpCommand, ChatRepository chatRepository) {
        super(COMMAND_IDENTIFIER, COMMAND_DESCRIPTION);
        this.helpCommand = helpCommand;
        this.chatRepository = chatRepository;
    }

    @Override
    public void execute(AbsSender absSender,
                        org.telegram.telegrambots.meta.api.objects.User user,
                        org.telegram.telegrambots.meta.api.objects.Chat chat,
                        String[] arguments) {
        String chatId = chat.getId().toString();

        if (!chatRepository.existsById(chatId)) {
            chatRepository.save(new Chat(chatId, new User(user.getFirstName(), user.getLastName(), user.getUserName(), null)));
        }

        try {
            absSender.execute(SendMessage.builder()
                    .chatId(chatId)
                    .text("WELCOME").build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        helpCommand.execute(absSender, user, chat, new String[]{});
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
