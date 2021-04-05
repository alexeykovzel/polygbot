package com.alexeykovzel;

import com.alexeykovzel.service.Emoji;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class onNonCommandUpdateTest {
    private static AbsSender absSender;
    private static String chatId;

    @BeforeAll
    static void setup() {
        chatId = "597554184";
        absSender = new DefaultAbsSender(ApiContext.getInstance(DefaultBotOptions.class)) {
            @Override
            public String getBotToken() {
                return "1402979569:AAEuPHqAzkc1cTYwGI7DXuVb76ZSptD4zPM";
            }
        };
    }

    @Test
    public void onNonCommandUpdate() {
        String[] messageTexts = {"test"};

        Document doc;
        for (String messageText : messageTexts) {
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
                    sendMsg(chatId, "Would you like to add '*" + origTerm + "*' to your word list?", buildWordAddReplyMarkup(origTerm));
//                    }
                } catch (NullPointerException e) {
                    sendMsg(chatId, "Ahh, I don't know what is '*" + messageText + "*' " + Emoji.DISAPPOINTED_BUT_RELIEVED_FACE);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

    private String toSearchForm(String text) {
        return text.toLowerCase().replace(" ", "-");
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

    private synchronized void sendMsg(String chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.disableWebPagePreview();
        try {
            absSender.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.getStackTrace();
        }
    }

    private synchronized void sendMsg(String chatId, String text, InlineKeyboardMarkup inlineKeyboardMarkup) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.disableWebPagePreview();
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        try {
            absSender.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.getStackTrace();
        }
    }

    private String escapeMarkdown(String text) {
        return text
                .replace("_", "\\_")
                .replace("*", "\\*")
                .replace("[", "\\[")
                .replace("`", "\\`");
    }
}
