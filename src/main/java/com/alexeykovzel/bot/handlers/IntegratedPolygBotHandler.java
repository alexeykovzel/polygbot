package com.alexeykovzel.bot.handlers;

import com.alexeykovzel.bot.commands.HelloCommand;
import com.alexeykovzel.bot.commands.HelpCommand;
import com.alexeykovzel.bot.commands.StartCommand;
import com.alexeykovzel.bot.MessageBuilder;
import com.alexeykovzel.db.entities.CaseStudy;
import com.alexeykovzel.db.entities.CaseStudyId;
import com.alexeykovzel.db.entities.Term;
import com.alexeykovzel.db.repositories.CaseStudyRepository;
import com.alexeykovzel.db.repositories.ChatRepository;
import com.alexeykovzel.db.repositories.TermRepository;
import com.alexeykovzel.services.CollinsDictionaryAPI;
import com.alexeykovzel.bot.Emoji;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.CommandRegistry;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

@Component
public class IntegratedPolygBotHandler extends LongPollingBotHandler {
    private static final Properties properties = new Properties();
    private final ChatRepository chatRepository;
    private final TermRepository termRepository;
    private final CaseStudyRepository caseStudyRepository;

    private static final String botToken = "1402979569:AAEuPHqAzkc1cTYwGI7DXuVb76ZSptD4zPM";
    private static final String botUsername = "polyg_bot";

    @Override
    public void onUpdateReceived(Update update) {
        handleUpdate(update);
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    public IntegratedPolygBotHandler(ChatRepository chatRepository, TermRepository termRepository, CaseStudyRepository caseStudyRepository) {
        commandRegistry = new CommandRegistry(true, () -> botUsername);
        HelpCommand helpCommand = new HelpCommand();
        commandRegistry.registerAll(helpCommand, new StartCommand(helpCommand, chatRepository), new HelloCommand());

        commandRegistry.registerDefaultAction((absSender, message) -> {
            try {
                absSender.execute(SendMessage.builder()
                        .chatId(message.getChatId().toString())
                        .text("The command '" + message.getText()
                                + "' is not known by this bot. Here comes some help " + Emoji.AMBULANCE).build());
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            helpCommand.execute(absSender, message.getFrom(), message.getChat(), new String[]{});
        });
        this.chatRepository = chatRepository;
        this.termRepository = termRepository;
        this.caseStudyRepository = caseStudyRepository;
    }

    public void processInvalidCommandUpdate(Update update) {
        String chatId = String.valueOf(update.getMessage().getChatId());
        sendMsg(chatId, "I don't know such command");
    }

    public void handleCallBackQuery(Update update) {
        CallbackQuery callbackquery = update.getCallbackQuery();
        Message message = callbackquery.getMessage();
        Integer messageId = message.getMessageId();
        String chatId = message.getChatId().toString();
        String command = callbackquery.getData();
        String commandQuery = null;

        if (command.contains("@")) {
            int separatorIndex = command.indexOf("@");
            commandQuery = command.substring(separatorIndex + 1);
            command = command.substring(0, separatorIndex);
        }

        switch (command) {
            case "saveWord":
                if (commandQuery != null) {
                    Term term = termRepository.findByValue(commandQuery);

                    if (term == null) {
                        term = Term.builder().value(commandQuery).build();
                        termRepository.save(term);
                    }

                    CaseStudy caseStudy = new CaseStudy(
                            term.getId(), chatId, 0.5, new Timestamp(System.currentTimeMillis()));
                    caseStudyRepository.save(caseStudy);

                    sendAnswerCallbackQuery("the word '" + commandQuery + "' is successfully added to your list!",
                            false, callbackquery);
                }
                break;
            case "notSaveWord":
                break;
        }
        deleteMsg(chatId, messageId);
    }

    public void processNonCommandUpdate(Update update) {
        Message message = update.getMessage();
        if (!message.hasText()) {
            processNonTextMessage(message);
        } else {
            processTextMessage(message);
        }
    }

    public void processNonTextMessage(Message message) {
        String chatId = message.getChatId().toString();
        sendMsg(chatId, "Send pls text");
    }

    public void processTextMessage(Message message) {
        String chatId = message.getChatId().toString();
        String messageText = message.getText();
        try {
            //get and send term details
            CollinsDictionaryAPI dictionaryAPI = new CollinsDictionaryAPI();
            Term.Details details = dictionaryAPI.getTermDetails(messageText);
            sendMsg(chatId, MessageBuilder.buildTermInfoMessage(details).toString());

            String termValue = details.getValue();

            Term term = termRepository.findByValue(termValue);
            boolean queryRequired;

            if (term != null) {
                queryRequired = !caseStudyRepository.findById(new CaseStudyId(term.getId(), chatId)).isPresent();
            } else {
                termRepository.save(Term.builder().value(termValue).build());
                queryRequired = true;
            }
            if (queryRequired) {
                sendMsg(chatId, "Would you like to learn '*" + termValue + "*'?", buildWordAddReplyMarkup(termValue));
            }
        } catch (IOException e) {
            sendMsg(chatId, "Ahh, I don't know what is '*" + messageText + "*' " + Emoji.DISAPPOINTED_BUT_RELIEVED_FACE);
        }
    }

    private InlineKeyboardMarkup buildWordAddReplyMarkup(String wordText) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowList = Collections.singletonList(
                Arrays.asList(
                        createInlineKeyboardButton("Actually, I do!", "saveWord@" + wordText),
                        createInlineKeyboardButton("Not really...", "notSaveWord@" + wordText)
                )
        );

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
}
