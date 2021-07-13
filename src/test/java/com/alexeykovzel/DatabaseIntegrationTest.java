package com.alexeykovzel;

import com.alexeykovzel.database.entity.*;
import com.alexeykovzel.database.repository.CaseStudyRepository;
import com.alexeykovzel.database.repository.ChatRepository;
import com.alexeykovzel.database.repository.TermRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = OnPolygBotLaunchTest.class)
@TestPropertySource(
        locations = "classpath:application-integration-test.properties")
public class DatabaseIntegrationTest {

    @Autowired
    private CaseStudyRepository caseStudyRepository;

    @Autowired
    private TermRepository termRepository;

    @Autowired
    private ChatRepository chatRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void checkIfCaseStudyExists() {
        String chatId = "597554184";
        String origTerm = "Hello";

        Chat chat = new Chat(chatId, new User("Aliaksei", null, "alexeykovzel", null));
        chatRepository.save(chat);

        Set<String> defs = new HashSet<>(Arrays.asList(
                "def1", "def2", "def3"));

        Set<String> egs = new HashSet<>(Arrays.asList(
                "eg1", "eg2", "eg3"));

        Term term = new Term(origTerm, defs, egs);
        termRepository.save(term);
        Long termId = term.getId();

        caseStudyRepository.save(new CaseStudy(termId, chatId, null, null));

        caseStudyRepository.findById(new CaseStudyId(term.getId(), chatId)).ifPresent(
                caseStudy -> System.out.println("\nCASE STUDY: " + caseStudy + "\n"));

        Term t = termRepository.findDetailedById(termId);
        System.out.println("TERM: " + t);

        System.out.println("\nDEFINITIONS:\n");
        t.getDefinitions().forEach(System.out::println);

        System.out.println("\nEXAMPLES:\n");
        t.getExamples().forEach(System.out::println);
        System.out.println("\n");
    }
}
