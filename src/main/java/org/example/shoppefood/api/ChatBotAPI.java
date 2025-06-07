package org.example.shoppefood.api;

import org.example.shoppefood.service.ProductChatService;
import org.example.shoppefood.dto.chatbot.ChatRequest;
import org.example.shoppefood.dto.chatbot.ChatResponse;
import org.example.shoppefood.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/chatbot")
@CrossOrigin("*") // Đảm bảo có thể gọi API từ localhost frontend
public class ChatBotAPI {

    @Autowired
    private ProductChatService productChatService;

    @Autowired
    private UserService userService;
    @PostMapping("/message")
    public ResponseEntity<ChatResponse> processMessage(@RequestBody ChatRequest request) {
        try {
            if (request == null || !StringUtils.hasText(request.getMessage())) {
                return ResponseEntity.badRequest()
                    .body(new ChatResponse("Vui lòng nhập tin nhắn."));
            }

            long userId = userService.getCurrentUserId();
            String userID = String.valueOf(userId);

            String response = productChatService.processMessage(request.getMessage(), userID);
            return ResponseEntity.ok(new ChatResponse(response));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new ChatResponse("Xin lỗi, đã có lỗi xảy ra. Vui lòng thử lại sau."));
        }
    }
}