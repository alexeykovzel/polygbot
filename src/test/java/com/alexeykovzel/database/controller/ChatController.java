package com.alexeykovzel.database.controller;

import com.alexeykovzel.database.entity.Chat;
import com.alexeykovzel.database.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "/chat")
public class ChatController {
    @Autowired
    private ChatRepository chatRepository;

    @PostMapping(path = "/add")
    public @ResponseBody
    String addChat() {

        Chat chat = new Chat();
        chat.setFirstName("Aliaksei");
        chatRepository.save(chat);
        return "Saved";
    }

    @GetMapping(path = "/all")
    public @ResponseBody
    Iterable<Chat> getAllChats() {
        return chatRepository.findAll();
    }
}
