package org.example.shoppefood.service.impl;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.dialogflow.v2.*;
import org.example.shoppefood.config.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.IOException;

@Service
public class DialogflowService {
    private static final Logger logger = LoggerFactory.getLogger(DialogflowService.class);

    @Value("${dialogflow.project-id}")
    private String projectId; // id dự án dialogflow trên Google Cloud

    @Value("${dialogflow.credentials-file}")
    private Resource credentialsFile; //File chứa thông tin xác thực

    @Autowired
    private SessionManager sessionManager;

    private SessionsClient sessionsClient; // sesionsClients để giao tiếp với dialogflow

    @PostConstruct
    public void init() throws IOException {
        try {
            GoogleCredentials credentials = GoogleCredentials.fromStream(
                    credentialsFile.getInputStream());

            SessionsSettings sessionsSettings = SessionsSettings.newBuilder()
                    .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                    .build();

            sessionsClient = SessionsClient.create(sessionsSettings);
            logger.info("DialogflowService initialized successfully");
        } catch (IOException e) {
            logger.error("Failed to initialize DialogflowService", e);
            throw e;
        }
    }

    public DetectIntentResponse detectIntent(String text, String userId) {
        try {
            // Lấy hoặc tạo session ID cho user
            String sessionId = sessionManager.getSessionId(userId);
            SessionName sessionName = SessionName.of(projectId, sessionId);

            // Tạo text input
            TextInput textInput = TextInput.newBuilder()
                    .setText(text)
                    .setLanguageCode("vi")
                    .build();

            // Tạo query input là loại dạng dialogflow hiểu được
            QueryInput queryInput = QueryInput.newBuilder()
                    .setText(textInput)
                    .build();

            // Tạo request
            DetectIntentRequest request = DetectIntentRequest.newBuilder()
                    .setSession(sessionName.toString())
                    .setQueryInput(queryInput)
                    .build();

            // Gửi request và nhận response
            DetectIntentResponse response = sessionsClient.detectIntent(request);
            logger.debug("Dialogflow response received for user {}: {}", userId, response.getQueryResult().getFulfillmentText());
            return response;
        } catch (Exception e) {
            logger.error("Error detecting intent for user {}: {}", userId, e.getMessage(), e);
            throw new RuntimeException("Failed to process message with Dialogflow", e);
        }
    }
}