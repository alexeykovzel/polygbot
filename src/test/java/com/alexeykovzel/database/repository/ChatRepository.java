package com.alexeykovzel.database.repository;

import com.alexeykovzel.database.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, String> {
    List<Chat> findByUserFirstName(String firstName);
}