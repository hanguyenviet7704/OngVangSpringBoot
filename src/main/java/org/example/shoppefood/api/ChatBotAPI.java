package org.example.shoppefood.api;


import org.example.shoppefood.config.ProductChatService;
import org.example.shoppefood.dto.chatbot.ChatRequest;
import org.example.shoppefood.dto.chatbot.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chatbot")
@CrossOrigin("*") // Đảm bảo có thể gọi API từ localhost frontend
public class ChatBotAPI {

    @Autowired
    private ProductChatService productChatService;

    @PostMapping("/message")
    public ResponseEntity<ChatResponse> processMessage(@RequestBody ChatRequest request) {
        String response = productChatService.processMessage(request.getMessage());
        return ResponseEntity.ok(new ChatResponse(response));
    }
}