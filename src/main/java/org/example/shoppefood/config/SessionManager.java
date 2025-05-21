package org.example.shoppefood.config;

import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.time.LocalDateTime;

@Component
public class SessionManager {
    private final Map<String, SessionInfo> sessions = new ConcurrentHashMap<>();
    private static final long SESSION_TIMEOUT_MINUTES = 30;

    public String createSession(String userId) {
        String sessionId = userId + "_" + System.currentTimeMillis();
        sessions.put(sessionId, new SessionInfo(userId));
        return sessionId;
    }

    public String getSessionId(String userId) {
        // Tìm session hiện tại của user
        for (Map.Entry<String, SessionInfo> entry : sessions.entrySet()) {
            if (entry.getValue().getUserId().equals(userId) && !isSessionExpired(entry.getValue())) {
                entry.getValue().updateLastAccess();
                return entry.getKey();
            }
        }
        // Nếu không tìm thấy hoặc session đã hết hạn, tạo session mới
        return createSession(userId);
    }

    public boolean isSessionExpired(SessionInfo session) {
        return LocalDateTime.now().isAfter(session.getLastAccess().plusMinutes(SESSION_TIMEOUT_MINUTES));
    }

    public void cleanupExpiredSessions() {
        sessions.entrySet().removeIf(entry -> isSessionExpired(entry.getValue()));
    }

    private static class SessionInfo {
        private final String userId;
        private LocalDateTime lastAccess;

        public SessionInfo(String userId) {
            this.userId = userId;
            this.lastAccess = LocalDateTime.now();
        }

        public String getUserId() {
            return userId;
        }

        public LocalDateTime getLastAccess() {
            return lastAccess;
        }

        public void updateLastAccess() {
            this.lastAccess = LocalDateTime.now();
        }
    }
} 