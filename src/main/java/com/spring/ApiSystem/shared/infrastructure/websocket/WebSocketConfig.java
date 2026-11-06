package com.spring.ApiSystem.shared.infrastructure.websocket;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private static final Logger log = LoggerFactory.getLogger(WebSocketConfig.class);

    private final StompAuthInterceptor stompAuthInterceptor;

    public WebSocketConfig(StompAuthInterceptor stompAuthInterceptor) {
        this.stompAuthInterceptor = stompAuthInterceptor;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        log.info("Registrando endpoint STOMP: /api/ws e /api/ws-native com origins permitidos: http://localhost:*, http://127.0.0.1:*, http://10.0.2.2:*, exp://*:* , https://csftreinamentos.me");
        registry.addEndpoint("/api/ws")
                .setAllowedOriginPatterns("http://localhost:*", "http://127.0.0.1:*", "http://10.0.2.2:*", "exp://*:*", "https://csftreinamentos.me")
                .withSockJS();

        // Support token passed as query parameter from native clients (e.g., ws://host:8080/api/ws-native?token=...)
        registry.addEndpoint("/api/ws-native")
            .setAllowedOriginPatterns("http://localhost:*", "http://127.0.0.1:*", "http://10.0.2.2:*", "exp://*:*", "https://csftreinamentos.me")
            .addInterceptors(new QueryParamHandshakeInterceptor());
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        log.info("Configurando broker STOMP: applicationPrefix=/app, userPrefix=/user, simpleBroker=[/topic, /queue]");
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");
        registry.enableSimpleBroker("/topic", "/queue");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompAuthInterceptor);
    }
}