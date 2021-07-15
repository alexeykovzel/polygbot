package com.alexeykovzel;

import com.alexeykovzel.db.repository.TermRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(
        locations = "classpath:application.properties")
public class TermRepositoryTest {
    private static final Log log = LogFactory.getLog(TermRepositoryTest.class);

    @Autowired
    private TermRepository termRepository;

    @Test
    void findIdByValue() {
        log.info(termRepository.findIdByValue("about"));
    }
}
