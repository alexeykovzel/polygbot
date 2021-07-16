package com.alexeykovzel.bot.command;

import com.alexeykovzel.db.repository.CaseStudyRepository;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.cache.CacheManager;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class VocabCommand extends BotCommand {
    private static final String COMMAND_IDENTIFIER = "vocab";
    private static final String COMMAND_DESCRIPTION = "shows user vocabulary";
    private final CaseStudyRepository caseStudyRepository;

    public VocabCommand(CaseStudyRepository caseStudyRepository) {
        super(COMMAND_IDENTIFIER, COMMAND_DESCRIPTION);
        this.caseStudyRepository = caseStudyRepository;
    }

    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        String chatId = chat.getId().toString();
        /*String messageId = null;
        boolean updateMarkup = false;
        int page = 1;

        if (arguments.length != 0) {
            String posStatus = arguments[0];
            if (!posStatus.equals("max") && !posStatus.equals("min")) {
                page = Integer.parseInt(arguments[0]);
                messageId = arguments[1];
                updateMarkup = true;
            }
        }*/

        caseStudyRepository.findAllTermValuesByChatId(chatId).ifPresent(termValues -> {
            StringBuilder message = new StringBuilder().append("This is your word list! " +
                    "Click the word to get its detailed info");

            String messageId = null;
            boolean updateMarkup = false;
            int maxTermsPerPage = 5;
            int page = 1;

            if (arguments.length != 0) {
                String posStatus = arguments[0];
                if (!posStatus.equals("max") && !posStatus.equals("min")) {
                    page = Integer.parseInt(arguments[0]);
                    messageId = arguments[1];
                    updateMarkup = true;
                }
            }

            int termsNum = termValues.size();
            int maxPage = (int) Math.ceil((double) termsNum / maxTermsPerPage);
            int indexI = (page - 1) * maxTermsPerPage;
            int indexF = page == maxPage ? termsNum : indexI + maxTermsPerPage;
            JSONArray termList = new JSONArray();

            for (int i = indexI; i < indexF; i++) {
                termList.put(new JSONObject()
                        .put("value", termValues.get(i))
                        .put("percent", 59));
            }


            try {
                if (updateMarkup) {
                    absSender.execute(EditMessageReplyMarkup.builder()
                            .chatId(chatId)
                            .messageId(Integer.valueOf(messageId))
                            .replyMarkup(buildWordAddReplyMarkup(termList, page, maxPage)).build());
                } else {
                    absSender.execute(SendMessage.builder()
                            .text(message.toString())
                            .chatId(chatId)
                            .replyMarkup(buildWordAddReplyMarkup(termList, page, maxPage))
                            .parseMode(ParseMode.MARKDOWN).build());
                }
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        });

    }

    private InlineKeyboardMarkup buildWordAddReplyMarkup(JSONArray jsonArray, int page, int maxPage) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            String termValue = object.getString("value");
            String btnText = termValue + " - " + object.getDouble("percent") + '%';

            rowList.add(Collections.singletonList(createBtn(btnText, new JSONObject()
                    .put("command", "select_term")
                    .put("page", page)
                    .put("value", termValue).toString())));
        }

        if (maxPage > 1) {
            List<InlineKeyboardButton> lastRow = new ArrayList<>();

            String pageData = page > 1 ? String.valueOf(page - 1) : "invalid";
            lastRow.add(createBtn("<",
                    new JSONObject().put("command", "turn_page").put("page", pageData).toString()));

            lastRow.add(createBtn("· " + page + " ·",
                    new JSONObject().put("command", "turn_page").put("page", page).toString()));

            pageData = page < maxPage ? String.valueOf(page + 1) : "invalid";
            lastRow.add(createBtn(">",
                    new JSONObject().put("command", "turn_page").put("page", pageData).toString()));

            rowList.add(lastRow);
        }

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    private InlineKeyboardButton createBtn(String text, String callbackData) {
        return InlineKeyboardButton.builder()
                .text(text)
                .callbackData(callbackData).build();
    }
}
