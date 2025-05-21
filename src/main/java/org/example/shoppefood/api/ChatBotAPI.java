package org.example.shoppefood.api;

import org.example.shoppefood.config.ProductChatService;
import org.example.shoppefood.dto.chatbot.ChatRequest;
import org.example.shoppefood.dto.chatbot.ChatResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/chatbot")
@CrossOrigin("*") // Đảm bảo có thể gọi API từ localhost frontend
public class ChatBotAPI {
    private static final Logger logger = LoggerFactory.getLogger(ChatBotAPI.class);

    @Autowired
    private ProductChatService productChatService;

    @PostMapping("/message")
    public ResponseEntity<ChatResponse> processMessage(@RequestBody ChatRequest request) {
        try {
            if (request == null || !StringUtils.hasText(request.getMessage())) {
                return ResponseEntity.badRequest()
                    .body(new ChatResponse("Vui lòng nhập tin nhắn."));
            }

            // Tạo một ID tạm thời cho user nếu chưa có
            String userId = request.getUserId();
            if (!StringUtils.hasText(userId)) {
                userId = UUID.randomUUID().toString();
            }

            String response = productChatService.processMessage(request.getMessage(), userId);
            return ResponseEntity.ok(new ChatResponse(response));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new ChatResponse("Xin lỗi, đã có lỗi xảy ra. Vui lòng thử lại sau."));
        }
    }
}