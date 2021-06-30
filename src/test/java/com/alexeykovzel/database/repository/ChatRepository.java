package com.alexeykovzel.database.repository;

import com.alexeykovzel.database.entity.Chat;
import org.springframework.data.repository.CrudRepository;

public interface ChatRepository extends CrudRepository<Chat, Integer> {
}