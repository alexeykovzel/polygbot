package com.alexeykovzel;

import com.alexeykovzel.database.entity.*;
import com.alexeykovzel.database.repository.CaseStudyRepository;
import com.alexeykovzel.database.repository.ChatRepository;
import com.alexeykovzel.database.repository.TermRepository;
import org.checkerframework.checker.nullness.Opt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;
import javax.swing.text.html.Option;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
public class onPolygBotLaunch {

    private static final Logger log = LoggerFactory.getLogger(onPolygBotLaunch.class);

    public static void main(String[] args) {
        SpringApplication.run(onPolygBotLaunch.class, args);
    }

    /*@Bean
    public CommandLineRunner demo(ChatRepository chatRepository,
                                  TermRepository termRepository,
                                  CaseStudyRepository caseStudyRepository) {
        return (args) -> {
            //...
        };
    }*/
}
