package com.alexeykovzel.database.repository;

import com.alexeykovzel.database.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, String> {
    List<Chat> findByUserFirstName(String firstName);
}