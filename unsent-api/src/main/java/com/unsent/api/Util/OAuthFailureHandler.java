package com.unsent.api.Util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class OAuthFailureHandler implements AuthenticationFailureHandler {

    private final String frontendBaseUrl;
    private final String frontendLoginPath;

    public OAuthFailureHandler(
            @Value("${app.frontend-url:http://localhost:5173}") String frontendBaseUrl,
            @Value("${app.frontend-login-path:/}") String frontendLoginPath
    ) {
        this.frontendBaseUrl = frontendBaseUrl;
        this.frontendLoginPath = frontendLoginPath;
    }

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException {
        String redirectUrl = UriComponentsBuilder
                .fromUriString(frontendBaseUrl)
                .path(frontendLoginPath)
                .queryParam("error", "oauth")
                .build(true)
                .toUriString();

        response.sendRedirect(redirectUrl);
    }
}
