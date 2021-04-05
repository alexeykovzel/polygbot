package com.alexeykovzel;

import com.alexeykovzel.repository.ChatRepository;
import org.junit.jupiter.api.Test;

public class addChatTest {

    @Test
    public void addChat() {
        ChatRepository.addChat("Aliaksei", "Kouzel", "aliakouz", 0.8);
    }
}
