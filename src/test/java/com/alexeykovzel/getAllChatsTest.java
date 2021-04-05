package com.alexeykovzel;

import com.alexeykovzel.repository.ChatRepository;
import org.junit.jupiter.api.Test;

public class getAllChatsTest {

    @Test
    public void getAllChats() {
        ChatRepository.getAllChats();
    }
}
