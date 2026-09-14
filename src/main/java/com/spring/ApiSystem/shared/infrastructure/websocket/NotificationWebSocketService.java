package com.spring.ApiSystem.shared.infrastructure.websocket;

import com.spring.ApiSystem.shared.infrastructure.websocket.dto.NotificationPayload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationWebSocketService {

    private static final String NOTIFICATIONS_DESTINATION = "/queue/notifications";

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationWebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendToUser(String email, NotificationPayload payload) {
        messagingTemplate.convertAndSendToUser(email, NOTIFICATIONS_DESTINATION, payload);
    }
}
