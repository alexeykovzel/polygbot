package com.alexeykovzel;

import com.alexeykovzel.database.entity.CaseStudy;
import com.alexeykovzel.database.entity.Chat;
import com.alexeykovzel.database.entity.Term;
import com.alexeykovzel.database.entity.User;
import com.alexeykovzel.database.repository.CaseStudyRepository;
import com.alexeykovzel.database.repository.ChatRepository;
import com.alexeykovzel.database.repository.TermRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = onPolygBotLaunch.class)
@TestPropertySource(
        locations = "classpath:application-integration-test.properties")
public class DatabaseIntegrationTest {

    @Autowired
    private CaseStudyRepository caseStudyRepository;

    @Autowired
    private TermRepository termRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Test
    void checkIfCaseStudyExists() {
        String chatId = "597554184";
        String origTerm = "Hello World!1";

        Chat chat = new Chat(chatId, new User("Aliaksei", null, "alexeykovzel", null));
        chatRepository.save(chat);

        Term term = new Term("Hello World!");
        termRepository.save(term);

        caseStudyRepository.save(new CaseStudy(term.getId(), chatId, null, null));
        List<CaseStudy> caseStudies = caseStudyRepository.findAllByChatId(chatId);

        boolean caseStudyExists = false;

        for (CaseStudy caseStudy : caseStudies) {
            if (caseStudy.getTerm().getValue().equals(origTerm)) {
                caseStudyExists = true;
                break;
            }
        }

        String statement = caseStudyExists ? "\nCASE STUDY EXISTS\n" : "\nCASE STUDY DOES NOT EXIST\n";
        System.out.println(statement);
    }
}
