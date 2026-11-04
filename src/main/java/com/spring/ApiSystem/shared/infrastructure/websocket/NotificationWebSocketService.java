package com.spring.ApiSystem.shared.infrastructure.websocket;

import com.spring.ApiSystem.shared.infrastructure.websocket.dto.NotificationPayload;
import com.spring.ApiSystem.shared.infrastructure.push.ExpoPushNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

@Service
public class NotificationWebSocketService {

    private static final Logger log = LoggerFactory.getLogger(NotificationWebSocketService.class);
    private static final String NOTIFICATIONS_DESTINATION = "/queue/notifications";

    private final SimpMessagingTemplate messagingTemplate;
    private final SimpUserRegistry simpUserRegistry;
    private final ExpoPushNotificationService expoPushNotificationService;

    public NotificationWebSocketService(SimpMessagingTemplate messagingTemplate,
                                        SimpUserRegistry simpUserRegistry,
                                        ExpoPushNotificationService expoPushNotificationService) {
        this.messagingTemplate = messagingTemplate;
        this.simpUserRegistry = simpUserRegistry;
        this.expoPushNotificationService = expoPushNotificationService;
    }

    public void sendToUser(String email, NotificationPayload payload) {
        boolean userConnected = isUserConnected(email);
        if (userConnected) {
            log.info("Enviando notificação WS para email={} destino={} payload={}", email, NOTIFICATIONS_DESTINATION, payload);
            messagingTemplate.convertAndSendToUser(email, NOTIFICATIONS_DESTINATION, payload);
            return;
        }

        log.info("Usuário offline em WS; enviando push para email={} payload={}", email, payload);
        expoPushNotificationService.sendToUser(email, payload);
    }

    private boolean isUserConnected(String email) {
        SimpUser user = simpUserRegistry.getUser(email);
        return user != null && !user.getSessions().isEmpty();
    }
}
