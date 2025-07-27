package com.techsavy.springai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

@RestController
public class ChatController {

    @Autowired
    private ChatClient chatClient;

    @GetMapping("/chat-with-text")
    public String chatWithText(@RequestParam(name = "message") String message) {
        return chatClient.prompt().user(message).call().content();
    }

    @GetMapping(path = "/stream-chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatWithStream(@RequestParam(name = "message") String message) {
        return chatClient.prompt().user(message).stream().content().doOnNext(System.out::print);
    }

    @PostMapping("/chat-with-image")
    public String chatWithImage(@RequestPart(name = "message") String message, @RequestPart(name = "image") MultipartFile file) {
        return chatClient.prompt().user(promptUserSpec -> {
            promptUserSpec.text(message);
            promptUserSpec.media(MediaType.IMAGE_PNG, new InputStreamResource(file));
        }).call().content();
    }
}
