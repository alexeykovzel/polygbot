package com.alexeykovzel.bot.commands;

import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * This command helps the user to find the command they need
 *
 * @author Timo Schulz (Mit0x2)
 */
public class HelpCommand extends BotCommand {
    private static final String COMMAND_IDENTIFIER = "help";
    private static final String COMMAND_DESCRIPTION = "shows all commands. Use /help [command] for more info";

    public HelpCommand() {
        super(COMMAND_IDENTIFIER, COMMAND_DESCRIPTION);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String text = "I am good at teaching new words and new meanings. You can start by writing /tutorial in the chat.\n" +
                "\n" +
                "Also, you can control me by using these commands:\n" +
                "\n" +
                "/help - shows all commands\n" +
                "/tutorial - learn to use this bot\n" +
                "\n" +
                "*Bot Usage*\n" +
                "/status - view user status\n" +
                "/vocab - open your vocabulary\n" +
                "/newquiz - take quick quiz\n" +
                "/newterm - suggest new term\n" +
                "\n" +
                "*Bot Settings*\n" +
                "/settings - change bot settings\n" +
                "/language - change bot language\n" +
                "/reset - reset user progress";

        SendMessage sendMessage = SendMessage.builder()
                .chatId(chat.getId().toString())
                .parseMode(ParseMode.MARKDOWN)
                .text(text).build();

        try {
            absSender.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}