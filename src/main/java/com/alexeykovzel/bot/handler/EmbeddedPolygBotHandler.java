package com.alexeykovzel.bot.handler;

import com.alexeykovzel.bot.command.HelpCommand;
import com.alexeykovzel.bot.command.StartCommand;
import com.alexeykovzel.bot.MessageBuilder;
import com.alexeykovzel.bot.command.VocabCommand;
import com.alexeykovzel.db.entity.CaseStudy;
import com.alexeykovzel.db.entity.CaseStudyId;
import com.alexeykovzel.db.entity.term.Term;
import com.alexeykovzel.db.entity.term.TermDef;
import com.alexeykovzel.db.entity.term.TermDto;
import com.alexeykovzel.db.repository.CaseStudyRepository;
import com.alexeykovzel.db.repository.ChatRepository;
import com.alexeykovzel.db.repository.TermRepository;
import com.alexeykovzel.service.CollinsDictionaryAPI;
import com.alexeykovzel.bot.Emoji;
import com.alexeykovzel.service.WebDictionary;
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
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class EmbeddedPolygBotHandler extends LongPollingBotHandler {
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

    public EmbeddedPolygBotHandler(ChatRepository chatRepository, TermRepository termRepository, CaseStudyRepository caseStudyRepository) {
        commandRegistry = new CommandRegistry(true, () -> botUsername);
        HelpCommand helpCommand = new HelpCommand();
        commandRegistry.registerAll(helpCommand, new StartCommand(helpCommand, chatRepository), new VocabCommand(chatRepository, termRepository, caseStudyRepository));

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
        String callbackData = callbackquery.getData();

        if (callbackData.contains("?")) {
            int separatorIndex = callbackData.indexOf("?");
            String command = callbackData.substring(0, separatorIndex);
            String params = callbackData.substring(separatorIndex + 1);

            Map<String, String> mappedParams = new HashMap<>();
            for (String param : params.split("&")) {
                String[] keyWithParam = param.split("=");
                mappedParams.put(keyWithParam[0], keyWithParam[1]);
            }

            switch (command) {
                case "saveWord":
                    String termValue = mappedParams.get("value");
                    Long termId = termRepository.findIdByValue(termValue);

                    CaseStudy caseStudy = new CaseStudy(
                            termId, chatId, 0.5, new Timestamp(System.currentTimeMillis()));
                    caseStudyRepository.save(caseStudy);

                    sendAnswerCallbackQuery("the word '" + termValue + "' is successfully added to your list!",
                            false, callbackquery);
                    deleteMsg(chatId, messageId);
                    break;
                case "notSaveWord":
                    deleteMsg(chatId, messageId);
                    break;
                case "selectTerm":
                    break;
            }
        }
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
        WebDictionary dictionary = new CollinsDictionaryAPI();
        try {
            //get and send term details
            TermDto termDto = dictionary.getTerm(messageText);
            sendMsg(chatId, MessageBuilder.buildTermInfoMessage(termDto).toString());

            String termValue = termDto.getValue();

            Long termId = termRepository.findIdByValue(termValue);
            boolean queryRequired;

            if (termId != null) {
                queryRequired = !caseStudyRepository.findById(new CaseStudyId(termId, chatId)).isPresent();
            } else {
                Set<String> termExamples = new HashSet<>(termDto.getExamples());

                Set<TermDef> termDefs = new HashSet<>();
                termDto.getDefs().forEach(def -> termDefs.add(new TermDef(def.getFirst(), def.getSecond())));

                termRepository.save(Term.builder()
                        .value(termValue)
                        .defs(termDefs)
                        .examples(termExamples).build());
                queryRequired = true;
            }
            if (queryRequired) {
                sendMsg(chatId, "Would you like to learn '*" + termValue + "*'?", buildWordAddReplyMarkup(termValue));
            }
        } catch (NullPointerException e) {
            sendMsg(chatId, "Ahh, I don't know what is '*" + messageText + "*' " + Emoji.DISAPPOINTED_BUT_RELIEVED_FACE);
        } catch (IOException e) {
            sendMsg(chatId, dictionary.getName() + " is not responding.. " + Emoji.FACE_WITH_COLD_SWEAT);
        }
    }

    private InlineKeyboardMarkup buildWordAddReplyMarkup(String wordText) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowList = Collections.singletonList(
                Arrays.asList(
                        createInlineKeyboardButton("Actually, I do!", "saveWord?value=" + wordText),
                        createInlineKeyboardButton("Not really...", "notSaveWord?value=" + wordText)
                )
        );

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
}
