package com.alexeykovzel.controller;

import com.alexeykovzel.commandRegistry.CommandRegistry;
import com.alexeykovzel.commandRegistry.command.HelloCommand;
import com.alexeykovzel.commandRegistry.command.HelpCommand;
import com.alexeykovzel.commandRegistry.command.StartCommand;
import com.alexeykovzel.service.Emoji;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.*;

public class PolygBotController extends BotController {
    private static final Properties properties = new Properties();
    private static PolygBotController polygBotController;

    public static synchronized PolygBotController getInstance(String botUsername, String botToken) {
        if (polygBotController == null) {
            polygBotController = new PolygBotController(botUsername, botToken);
        }
        return polygBotController;
    }

    public PolygBotController(String botUsername, String botToken) {
        absSender = absSender(botToken);
        commandRegistry = new CommandRegistry(true, botUsername);
        HelpCommand helpCommand = new HelpCommand(commandRegistry);
        commandRegistry.register(helpCommand);
        commandRegistry.register(new HelloCommand());
        commandRegistry.register(new StartCommand(helpCommand));

        commandRegistry.registerDefaultAction((absSender, message) -> {
            SendMessage commandUnknownMessage = new SendMessage();
            commandUnknownMessage.setChatId(String.valueOf(message.getChatId()));
            commandUnknownMessage.setText("The command '" + message.getText() + "' is not known by this bot. Here comes some help " + Emoji.AMBULANCE);
            try {
                absSender.execute(commandUnknownMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            helpCommand.execute(absSender, message.getFrom(), message.getChat(), new String[]{});
        });
    }

    @Override
    public void processInvalidCommandUpdate(Update update) {
        String chatId = String.valueOf(update.getMessage().getChatId());
        sendMsg(chatId, "I don't know such command");
    }

    @Override
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

                sendMsg(chatId, "Saved successfully!");
                    /*if (!WordHome.isDublicate(chatId, commandQuery)) {
                        assert commandQuery != null;
                        WordHome.saveWord(chatId, commandQuery, 0.7);
                        sendAnswerCallbackQuery("the word '" + commandQuery + "' is successfully added to your list!", false, callbackquery);
                    }*/
                break;
            case "notSaveWord":
                break;
        }
        deleteMsg(chatId, messageId);
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        Message message = update.getMessage();
        if (!message.hasText()) {
            processNonTextMessage(message);
        } else {
            processTextMessage(message);
        }
    }

    @Override
    public void processNonTextMessage(Message message) {
        String chatId = message.getChatId().toString();
        sendMsg(chatId, "Send pls text");
    }

    @Override
    public void processTextMessage(Message message) {
        String chatId = message.getChatId().toString();
        String messageText = message.getText();
        Document doc;
        try {
            String searchQuery = "https://www.collinsdictionary.com/dictionary/english/" + toSearchForm(messageText);
            doc = Jsoup.connect(searchQuery).get();
            try {
                Element body = doc.body();
                String origTerm = body.getElementsByClass("orth").first().text();

                // send term info message
                sendMsg(chatId, buildTermInfoMessage(body, origTerm, searchQuery).toString());

                // send message query to add a term to user local vocabulary
//                    if (!WordHome.isDublicate(chatId, trueTerm)) {
                sendMsg(chatId, "Would you like to add '*" + origTerm + "*' to your local vocabulary?", buildWordAddReplyMarkup(origTerm));
//                    }
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
                        new InlineKeyboardButton().setText("Actually, I do!").setCallbackData("saveWord@" + wordText),
                        new InlineKeyboardButton().setText("Not really...").setCallbackData("notSaveWord@" + wordText)
                )
        );

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
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
        response.append("\n[More](").append(searchQuery).append(") about '*").append(origTerm).append("*'");

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
            StringBuilder exampleSection = new StringBuilder();
            if (examplesList.size() >= maxValue) {
                for (int i = 0; i < maxValue; i++) {
                    exampleSection.append("- ").append(examplesList.get(i).text()).append("\n");
                }
            } else {
                for (Element exampleElement : examplesList) {
                    exampleSection.append("- ").append(exampleElement.text()).append("\n");
                }
            }
            examplesSection.append(escapeMarkdown(exampleSection.toString()));
        }
        return examplesSection;
    }

    private String toSearchForm(String text) {
        return text.toLowerCase().replace(" ", "-");
    }
}
