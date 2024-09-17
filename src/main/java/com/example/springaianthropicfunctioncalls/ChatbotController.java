package com.example.springaianthropicfunctioncalls;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.Objects;

@RestController
@RequestMapping("/chatbot")
public class ChatbotController {
    private static final Logger log = LoggerFactory.getLogger(ChatbotController.class);

    private final ChatClient chatClient;
    public ChatbotController(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultSystem("You are a helpful AI Assistant answering questions about cities around the world.")
                .defaultFunctions("currentWeatherFunction")
                .build();
    }

    public record Request(String message) {}
    public record Response(String content) {}

    @PostMapping("/")
    public Response nonStream(@RequestBody Request request) {
        log.info("Non streaming chat request: {}", request);
        ChatResponse chatResponse = chatClient.prompt()
                .user(request.message())
                .call()
                .chatResponse();

        return mapToResponse(chatResponse);
    }


    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Response> stream(@RequestBody Request request) {
        log.info("Streaming chat request: {}", request);
        return chatClient.prompt()
                .user(request.message())
                .stream()
                .chatResponse()
                .map(this::mapToResponse);
    }

    private Response mapToResponse(ChatResponse chatResponse) {
        return new Response(getContent(chatResponse));
    }

    private String getContent(ChatResponse chatResponse) {
        Objects.requireNonNull(chatResponse, "chatResponse cannot be null");
        if (chatResponse.getResult() == null) {
            return "";
        }

        if (chatResponse.getResult().getOutput() == null) {
            return "";
        }
        if (chatResponse.getResult().getOutput().getContent() == null) {
            return "";
        }
        return chatResponse.getResult().getOutput().getContent();
    }
}
