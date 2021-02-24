package com.alexeykovzel.controller;

import com.alexeykovzel.commandRegistry.CommandRegistry;
import com.alexeykovzel.commandRegistry.command.HelloCommand;
import com.alexeykovzel.commandRegistry.command.HelpCommand;
import com.alexeykovzel.commandRegistry.command.StartCommand;
import com.alexeykovzel.service.Emoji;
import com.amazonaws.services.lambda.runtime.Context;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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
        String chatId = String.valueOf(message.getChatId());
        if (message.hasText()) {
            String messageText = message.getText();
            Document doc;
            StringBuilder response = new StringBuilder();
            try {
                String searchQuery = "https://www.collinsdictionary.com/dictionary/english/" + toSearchForm(messageText);
                doc = Jsoup.connect(searchQuery).get();
                try {
                    Element body = doc.body();
                    body.getElementsByClass("hi rend-sup").remove();
                    String trueTerm = body.getElementsByClass("orth").first().text();
                    Elements defList = body.getElementsByClass("definitions").first().getElementsByClass("hom");

                    // Add definitions list
                    if (!defList.isEmpty()) {
                        List<String> defTextList = new ArrayList<>();
                        for (Element defElement : defList) {
                            String defText = getDefText(defElement);
                            if (defText != null) {
                                defTextList.add(defText);
                                if (defTextList.size() == 2) {
                                    break;
                                }
                            }
                        }
                        for (String text : defTextList) {
                            response.append(text);
                        }
                    }

                    // Add example list
                    Elements exampleList = body.getElementsByClass("quote");
                    if (!exampleList.isEmpty()) {
                        response.append("*EXAMPLES*\n\n");
                        StringBuilder examples = new StringBuilder();
                        int examplesNumber = 3;
                        if (exampleList.size() >= examplesNumber) {
                            for (int i = 0; i <= examplesNumber - 1; i++) {
                                examples.append("- ").append(exampleList.get(i).text()).append("\n");
                            }
                        } else {
                            for (int i = 0; i <= exampleList.size() - 1; i++) {
                                examples.append("- ").append(exampleList.get(i).text()).append("\n");
                            }
                        }
                        response.append(escapeMarkdown(examples.toString()));
                    }
                    response.append("\n[More](").append(searchQuery).append(") about '*").append(trueTerm).append("*'");

                    sendMsg(chatId, response.toString());
//                        if (!WordHome.isDublicate(chatId, trueTerm)) {
                    sendMsg(chatId, "Would you like to add '*" + trueTerm + "*' to your word list?", getWordAddingReplyMarkup(trueTerm));
//                        }
                } catch (NullPointerException e) {
                    sendMsg(chatId, "Ahh, I don't know what is '*" + messageText + "*' " + Emoji.DISAPPOINTED_BUT_RELIEVED_FACE);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            sendMsg(chatId, "Send pls text");
        }
    }

    private String getDefText(Element def) {
        StringBuilder defText = new StringBuilder();
        try {
            try {
                String partOfSpeech = def.getElementsByClass("pos").first().text();
                defText.append("*[").append(escapeMarkdown(partOfSpeech.toUpperCase())).append("]* ");
            } catch (Exception ignored) {
            }
            defText.append(def.getElementsByClass("def").first().text()).append("\n\n");
        } catch (Exception ignored) {
            return null;
        }

        return defText.toString();
    }

    private String toSearchForm(String text) {
        return text.toLowerCase().replace(" ", "-");
    }

    private InlineKeyboardMarkup getWordAddingReplyMarkup(String wordText) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> row1 = new ArrayList<>();

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("Actually, I do!");
        button1.setCallbackData("saveWord@" + wordText);

        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("Not really...");
        button2.setCallbackData("notSaveWord@" + wordText);

        row1.add(button1);
        row1.add(button2);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(row1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
}
