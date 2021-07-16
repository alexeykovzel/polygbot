package com.alexeykovzel;

import com.alexeykovzel.db.repository.CaseStudyRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(
        locations = "classpath:application.properties")
class CaseStudyRepositoryTest {
    private static final Logger logger = LoggerFactory.getLogger(CaseStudyRepositoryTest.class);

    @Autowired
    private CaseStudyRepository caseStudyRepository;

    @Autowired
    private CacheManager cacheManager;

    @Test
    void checkCashing() {
        String chatId = "597554184";

        Optional<List<String>> termValues = findAllTermValuesByChatId(chatId);
    }

    Optional<List<String>> findAllTermValuesByChatId(String chatId) {
        return caseStudyRepository.findAllTermValuesByChatId(chatId);
    }
}