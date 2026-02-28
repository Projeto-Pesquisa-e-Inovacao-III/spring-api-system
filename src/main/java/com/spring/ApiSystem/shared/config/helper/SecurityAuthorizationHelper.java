package com.spring.ApiSystem.shared.config.helper;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authorization.AuthenticatedAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.Arrays;
@Component
public class SecurityAuthorizationHelper {

    public boolean handlerExists(HttpServletRequest request, HandlerMappingIntrospector introspector) {
        try {
            HandlerMapping mapping = introspector.getMatchableHandlerMapping(request);
            if (mapping == null) return false;

            HandlerExecutionChain chain = mapping.getHandler(request);
            return chain != null;
        } catch (Exception ex) {
            return true;
        }
    }

    public AuthorizationManager<RequestAuthorizationContext> authenticatedIfExists(
            HandlerMappingIntrospector introspector
    ) {
        return (authentication, context) -> {
            HttpServletRequest request = context.getRequest();

            if (!handlerExists(request, introspector)) {
                return new AuthorizationDecision(true);
            }

            return AuthenticatedAuthorizationManager.authenticated()
                    .check(authentication, context);
        };
    }

    public AuthorizationManager<RequestAuthorizationContext> roleIfExists(
            HandlerMappingIntrospector introspector,
            String roleAuthority
    ) {
        return (authentication, context) -> {
            HttpServletRequest request = context.getRequest();

            if (!handlerExists(request, introspector)) {
                return new AuthorizationDecision(true);
            }

            var authDecision = AuthenticatedAuthorizationManager.authenticated()
                    .check(authentication, context);
            if (!authDecision.isGranted()) return authDecision;

            boolean hasRole = authentication.get().getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals(roleAuthority));

            return new AuthorizationDecision(hasRole);
        };
    }

    public AuthorizationManager<RequestAuthorizationContext> anyRoleIfExists(
            HandlerMappingIntrospector introspector,
            String... roleAuthorities
    ) {
        return (authentication, context) -> {
            HttpServletRequest request = context.getRequest();

            if (!handlerExists(request, introspector)) {
                return new AuthorizationDecision(true);
            }

            var authDecision = AuthenticatedAuthorizationManager.authenticated()
                    .check(authentication, context);
            if (!authDecision.isGranted()) return authDecision;

            var granted = authentication.get().getAuthorities().stream()
                    .map(a -> a.getAuthority())
                    .anyMatch(a -> Arrays.asList(roleAuthorities).contains(a));

            return new AuthorizationDecision(granted);
        };
    }

}
