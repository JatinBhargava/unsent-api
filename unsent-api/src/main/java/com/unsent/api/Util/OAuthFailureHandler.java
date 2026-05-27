package com.unsent.api.Util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class OAuthFailureHandler implements AuthenticationFailureHandler {

    private static final Logger log = LoggerFactory.getLogger(OAuthFailureHandler.class);
    private final String frontendBaseUrl;

    public OAuthFailureHandler(
            @Value("${app.frontend-url}") String frontendBaseUrl
    ) {
        this.frontendBaseUrl = frontendBaseUrl;
    }

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException {
        log.error(
                "OAuth authentication failure: uri={}, error={}",
                request.getRequestURI(),
                exception.getMessage(),
                exception
        );

        String redirectUrl = UriComponentsBuilder
                .fromUriString(frontendBaseUrl)
                .path("/")
                .queryParam("error", "oauth")
                .queryParam("reason", "provider_failure")
                .build(true)
                .toUriString();

        response.sendRedirect(redirectUrl);
    }
}
