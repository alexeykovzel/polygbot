package com.alexeykovzel.commandRegistry;

import com.alexeykovzel.commandRegistry.command.BotCommand;
import com.alexeykovzel.commandRegistry.command.IBotCommand;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public final class CommandRegistry implements ICommandRegistry {
    private final Map<String, IBotCommand> commandRegistryMap = new HashMap<>();
    private final boolean allowCommandsWithUsername;
    private final String botUsername;
    private BiConsumer<AbsSender, Message> defaultConsumer;

    /**
     * Creates a Command registry
     *
     * @param allowCommandsWithUsername True to allow commands with username, false otherwise
     * @param botUsername               Bot username
     */
    public CommandRegistry(boolean allowCommandsWithUsername, String botUsername) {
        this.allowCommandsWithUsername = allowCommandsWithUsername;
        this.botUsername = botUsername;
    }

    @Override
    public void registerDefaultAction(BiConsumer<AbsSender, Message> defaultConsumer) {
        this.defaultConsumer = defaultConsumer;
    }

    @Override
    public final boolean register(IBotCommand botCommand) {
        if (commandRegistryMap.containsKey(botCommand.getCommandIdentifier())) {
            return false;
        }
        commandRegistryMap.put(botCommand.getCommandIdentifier(), botCommand);
        return true;
    }

    @Override
    public final Map<IBotCommand, Boolean> registerAll(IBotCommand... botCommands) {
        Map<IBotCommand, Boolean> resultMap = new HashMap<>(botCommands.length);
        for (IBotCommand botCommand : botCommands) {
            resultMap.put(botCommand, register(botCommand));
        }
        return resultMap;
    }

    @Override
    public final boolean deregister(IBotCommand botCommand) {
        if (commandRegistryMap.containsKey(botCommand.getCommandIdentifier())) {
            commandRegistryMap.remove(botCommand.getCommandIdentifier());
            return true;
        }
        return false;
    }

    @Override
    public final Map<IBotCommand, Boolean> deregisterAll(IBotCommand... botCommands) {
        Map<IBotCommand, Boolean> resultMap = new HashMap<>(botCommands.length);
        for (IBotCommand botCommand : botCommands) {
            resultMap.put(botCommand, deregister(botCommand));
        }
        return resultMap;
    }

    @Override
    public final Collection<IBotCommand> getRegisteredCommands() {
        return commandRegistryMap.values();
    }

    @Override
    public final IBotCommand getRegisteredCommand(String commandIdentifier) {
        return commandRegistryMap.get(commandIdentifier);
    }

    /**
     * Executes a command action if the command is registered.
     *
     * @param absSender absSender
     * @param message   input message
     * @return True if a command or default action is executed, false otherwise
     * @note If the command is not registered and there is a default consumer,
     * that action will be performed
     */
    public final boolean executeCommand(AbsSender absSender, Message message) {
        if (message.hasText()) {
            String text = message.getText();
            if (text.startsWith(BotCommand.COMMAND_INIT_CHARACTER)) {
                String commandMessage = text.substring(1);
                String[] commandSplit = commandMessage.split(BotCommand.COMMAND_PARAMETER_SEPARATOR_REGEXP);

                String command = removeUsernameFromCommandIfNeeded(commandSplit[0]);

                if (commandRegistryMap.containsKey(command)) {
                    String[] parameters = Arrays.copyOfRange(commandSplit, 1, commandSplit.length);
                    commandRegistryMap.get(command).processMessage(absSender, message, parameters);
                    return true;
                } else if (defaultConsumer != null) {
                    defaultConsumer.accept(absSender, message);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * if {@link #allowCommandsWithUsername} is enabled, the username of the bot is removed from
     * the command
     *
     * @param command Command to simplify
     * @return Simplified command
     */
    private String removeUsernameFromCommandIfNeeded(String command) {
        if (allowCommandsWithUsername) {
            return command.replace("@" + botUsername, "").trim();
        }
        return command;
    }
}