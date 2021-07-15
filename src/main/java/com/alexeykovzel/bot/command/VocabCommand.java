package com.alexeykovzel.bot.command;

import com.alexeykovzel.db.entity.CaseStudy;
import com.alexeykovzel.db.entity.Chat;
import com.alexeykovzel.db.entity.term.Term;
import com.alexeykovzel.db.repository.CaseStudyRepository;
import com.alexeykovzel.db.repository.ChatRepository;
import com.alexeykovzel.db.repository.TermRepository;
import com.alexeykovzel.util.Pair;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

public class VocabCommand extends BotCommand {
    private static final String COMMAND_IDENTIFIER = "vocab";
    private static final String COMMAND_DESCRIPTION = "shows user vocabulary";
    private final ChatRepository chatRepository;
    private final TermRepository termRepository;
    private final CaseStudyRepository caseStudyRepository;

    public VocabCommand(ChatRepository chatRepository, TermRepository termRepository, CaseStudyRepository caseStudyRepository) {
        super(COMMAND_IDENTIFIER, COMMAND_DESCRIPTION);
        this.chatRepository = chatRepository;
        this.termRepository = termRepository;
        this.caseStudyRepository = caseStudyRepository;
    }

    public void execute(AbsSender absSender,
                        org.telegram.telegrambots.meta.api.objects.User user,
                        org.telegram.telegrambots.meta.api.objects.Chat chat,
                        String[] arguments) {
        String chatId = chat.getId().toString();

        StringBuilder message = new StringBuilder().append("This is your word list! " +
                "Click the word to get its detailed info");

        List<Pair<String, String>> rows = new ArrayList<>();
        caseStudyRepository.findAllTermValuesByChatId(chatId).ifPresent(termValues ->
                termValues.forEach(termValue -> rows.add(new Pair<>(termValue, "59%"))));

        try {
            absSender.execute(SendMessage.builder()
                    .text(message.toString())
                    .chatId(chatId)
                    .replyMarkup(buildWordAddReplyMarkup(rows))
                    .parseMode(ParseMode.MARKDOWN).build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private InlineKeyboardMarkup buildWordAddReplyMarkup(List<Pair<String, String>> rows) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        int count = 1;
        for (Pair<String, String> termInfo : rows) {
            rowList.add(Collections.singletonList(
                    createInlineKeyboardButton(
                            count + ". " + termInfo.getFirst() + " - " + termInfo.getSecond(),
                            "selectTerm?page=" + (count / 10 + 1)
                                    + "&value=" + termInfo.getFirst()
                                    + "&percent=" + termInfo.getSecond()
                    )));
            count++;
        }

        rowList.add(Arrays.asList(
                createInlineKeyboardButton("<<", "turnPage@"),
                createInlineKeyboardButton("<", "turnPage@"),
                createInlineKeyboardButton("-", "turnPage@"),
                createInlineKeyboardButton(">", "turnPage@"),
                createInlineKeyboardButton(">>", "turnPage@")));

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    private InlineKeyboardButton createInlineKeyboardButton(String text, String callbackData) {
        return InlineKeyboardButton.builder()
                .text(text)
                .callbackData(callbackData).build();
    }
}
