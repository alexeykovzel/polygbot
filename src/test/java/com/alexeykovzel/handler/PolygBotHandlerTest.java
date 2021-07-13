package com.alexeykovzel.handler;

import com.alexeykovzel.command.HelloCommand;
import com.alexeykovzel.command.HelpCommand;
import com.alexeykovzel.command.StartCommand;
import com.alexeykovzel.database.entity.CaseStudy;
import com.alexeykovzel.database.entity.CaseStudyId;
import com.alexeykovzel.database.entity.Term;
import com.alexeykovzel.database.repository.CaseStudyRepository;
import com.alexeykovzel.database.repository.ChatRepository;
import com.alexeykovzel.database.repository.TermRepository;
import com.alexeykovzel.service.Emoji;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
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
public class PolygBotHandlerTest extends BotHandlerTest {
    private static final Properties properties = new Properties();
    private static PolygBotHandler polygBotController;

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

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private TermRepository termRepository;

    @Autowired
    private CaseStudyRepository caseStudyRepository;

    public PolygBotHandlerTest() {
        commandRegistry = new CommandRegistry(true, () -> botUsername);
        HelpCommand helpCommand = new HelpCommand();
        commandRegistry.registerAll(helpCommand, new StartCommand(helpCommand), new HelloCommand());

        commandRegistry.registerDefaultAction((absSender, message) -> {
            try {
                absSender.execute(SendMessage.builder()
                        .chatId(message.getChatId().toString())
                        .text("The command '" + message.getText() + "' is not known by this bot. Here comes some help " + Emoji.AMBULANCE).build());
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            helpCommand.execute(absSender, message.getFrom(), message.getChat(), new String[]{});
        });
    }

    public void processInvalidCommandUpdate(Update update) {
        String chatId = String.valueOf(update.getMessage().getChatId());
        sendMsg(chatId, "I don't know such command");
    }

    public void handleCallBackQuery(Update update) {
        CallbackQuery callbackquery = update.getCallbackQuery();
        Message message = callbackquery.getMessage();
        Integer messageId = message.getMessageId();
        String chatId = String.valueOf(message.getChatId());
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
                        term = new Term(commandQuery);
                        termRepository.save(term);
                    }

                    CaseStudy caseStudy = new CaseStudy(
                            term.getId(), chatId, 0.5, new Timestamp(System.currentTimeMillis()));
                    caseStudyRepository.save(caseStudy);

                    sendAnswerCallbackQuery("the word '" + commandQuery + "' is successfully added to your list!", false, callbackquery);
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
        Document doc;
        try {
            String searchQuery = "https://www.collinsdictionary.com/dictionary/english/" + toSearchForm(messageText);
            doc = Jsoup.connect(searchQuery).get();
            try {
                Element body = doc.body();
                String termValue = body.getElementsByClass("orth").first().text();

                // send term info message
                sendMsg(chatId, buildTermInfoMessage(body, termValue, searchQuery).toString());

                // send message query to add a term to user local vocabulary

                Term term = termRepository.findByValue(termValue);
                boolean queryRequired;

                if (term != null) {
                    queryRequired = !caseStudyRepository.findById(new CaseStudyId(term.getId(), chatId)).isPresent();
                } else {
                    termRepository.save(new Term(termValue));
                    queryRequired = true;
                }
                if (queryRequired) {
                    sendMsg(chatId, "Would you like to learn '*" + termValue + "*'?", buildWordAddReplyMarkup(termValue));
                }
            } catch (NullPointerException e) {
                sendMsg(chatId, "Ahh, I don't know what is '*" + messageText + "*' " + Emoji.DISAPPOINTED_BUT_RELIEVED_FACE);
            }
        } catch (IOException e) {
            e.printStackTrace();
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

    private InlineKeyboardButton createInlineKeyboardButton(String text, String callbackData) {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(text);
        inlineKeyboardButton.setCallbackData(callbackData);
        return inlineKeyboardButton;
    }

    private StringBuilder buildTermInfoMessage(Element body, String origTerm, String searchQuery) {
        StringBuilder response = new StringBuilder();
        Elements defList = body.getElementsByClass("hom");
        Elements examplesList = body.getElementsByClass("quote");

        // Add 'definitions' section
        response.append(getDefSection(defList, 2));

        // Add 'examples' section
        response.append(getExamplesSection(examplesList, 3));

        // Add 'more' section
        response.append("[More](").append(searchQuery).append(") about '*").append(origTerm).append("*'");

        return response;
    }

    private StringBuilder getDefSection(Elements defList, int maxValue) {
        StringBuilder defSection = new StringBuilder();
        if (!defList.isEmpty()) {

            if (defList.size() >= maxValue) {
                for (int i = 0; i < maxValue; i++) {
                    defSection.append(getDefText(defList.get(i)));
                }
            } else {
                for (Element defElement : defList) {
                    defSection.append(getDefText(defElement));
                }
            }
        }
        return defSection;
    }

    private String getDefText(Element defSection) {
        StringBuilder defText = new StringBuilder();

        Element defElement = defSection.getElementsByClass("def").first();
        if (defElement != null) {
            Element posElement = defSection.getElementsByClass("pos").first();
            if (posElement != null) {
                defText.append("*[").append(escapeMarkdown(posElement.text().toUpperCase())).append("]* ");
            }
            defText.append(defElement.text()).append("\n\n");
        }
        return defText.toString();
    }

    private StringBuilder getExamplesSection(Elements examplesList, int maxValue) {
        StringBuilder examplesSection = new StringBuilder();

        if (!examplesList.isEmpty()) {
            examplesSection.append("*EXAMPLES*\n\n");
            StringBuilder examples = new StringBuilder();
            if (examplesList.size() >= maxValue) {
                for (int i = 0; i < maxValue; i++) {
                    examples.append("- ").append(examplesList.get(i).text()).append("\n");
                }
            } else {
                for (Element exampleElement : examplesList) {
                    examples.append("- ").append(exampleElement.text()).append("\n");
                }
            }
            examplesSection.append(escapeMarkdown(examples.toString())).append("\n");
        }
        return examplesSection;
    }

    private String toSearchForm(String text) {
        return text.toLowerCase().replace(" ", "-");
    }
}
