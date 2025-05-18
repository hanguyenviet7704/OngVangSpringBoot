package org.example.shoppefood.service.impl;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.dialogflow.v2.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.UUID;

@Service
public class DialogflowService {

    @Value("${dialogflow.project-id}")
    private String projectId;

    @Value("${dialogflow.credentials-file}")
    private Resource credentialsFile;

    private SessionsClient sessionsClient;
    private SessionName sessionName;
    private String sessionId;

    @PostConstruct
    public void init() throws IOException {
        sessionId = UUID.randomUUID().toString();

        GoogleCredentials credentials = GoogleCredentials.fromStream(
                credentialsFile.getInputStream());

        SessionsSettings sessionsSettings = SessionsSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();

        sessionsClient = SessionsClient.create(sessionsSettings);
        sessionName = SessionName.of(projectId, sessionId);
    }

    public DetectIntentResponse detectIntent(String text) {
        // Tạo text input
        TextInput textInput = TextInput.newBuilder()
                .setText(text)
                .setLanguageCode("vi") // Đổi thành "vi" cho tiếng Việt
                .build();

        // Tạo query input
        QueryInput queryInput = QueryInput.newBuilder()
                .setText(textInput)
                .build();

        // Tạo request
        DetectIntentRequest request = DetectIntentRequest.newBuilder()
                .setSession(sessionName.toString())
                .setQueryInput(queryInput)
                .build();

        // Gửi request và nhận response
        return sessionsClient.detectIntent(request);
    }
}