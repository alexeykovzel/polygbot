package com.alexeykovzel;

import com.alexeykovzel.database.entity.Chat;
import com.alexeykovzel.database.repository.ChatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class onPolygBotLaunch {
    private static final Logger log = LoggerFactory.getLogger(onPolygBotLaunch.class);

    public static void main(String[] args) {
        SpringApplication.run(onPolygBotLaunch.class, args);
    }

    @Bean
    public CommandLineRunner demo(ChatRepository repository) {
        return (args) -> {
            repository.save(new Chat("1", "Ivan", "Ivanov", "ivan123", null));
            log.info(repository.findByFirstName("Ivan").stream().findFirst().toString());
        };
    }
}
