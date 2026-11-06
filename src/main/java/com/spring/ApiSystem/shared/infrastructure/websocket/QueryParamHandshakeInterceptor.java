package com.spring.ApiSystem.shared.infrastructure.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.net.URI;
import java.util.Map;

public class QueryParamHandshakeInterceptor implements HandshakeInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(QueryParamHandshakeInterceptor.class);

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        URI uri = request.getURI();
        if (uri != null && uri.getQuery() != null) {
            String[] params = uri.getQuery().split("&");
            for (String p : params) {
                int idx = p.indexOf('=');
                if (idx > 0) {
                    String key = p.substring(0, idx);
                    String val = p.substring(idx + 1);
                    if ("token".equals(key) && val != null && !val.isBlank()) {
                        attributes.put("jwt", java.net.URLDecoder.decode(val, java.nio.charset.StandardCharsets.UTF_8));
                        logger.debug("Handshake: extracted token from query param and stored in session attributes");
                        break;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, @Nullable Exception exception) {
        // no-op
    }
}
