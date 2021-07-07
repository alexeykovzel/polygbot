package com.alexeykovzel.ability;

import com.alexeykovzel.commandRegistry.ICommandRegistry;
import com.alexeykovzel.commandRegistry.command.BotCommand;
import com.alexeykovzel.commandRegistry.command.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class HelpAbility extends BotCommand {
    private final ICommandRegistry commandRegistry;

    public HelpAbility(ICommandRegistry commandRegistry) {
        super("help", "Get all the commands this bot provides");
        this.commandRegistry = commandRegistry;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        StringBuilder helpMessageBuilder = new StringBuilder();
        helpMessageBuilder.append("You can send me any words/collocations that you want to know.\n\n*Bot Commands*\n");

        for (IBotCommand botCommand : commandRegistry.getRegisteredCommands()) {
            helpMessageBuilder.append("/").append(botCommand.getCommandIdentifier()).append(" - ").append(botCommand.getDescription()).append("\n");
        }

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chat.getId().toString());
        sendMessage.enableMarkdown(true);
        sendMessage.setText(helpMessageBuilder.toString());

        try {
            absSender.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}