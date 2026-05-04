package com.unsent.api.Util;

import com.unsent.api.config.JWTUtil;
import com.unsent.api.entity.User;
import com.unsent.api.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(OAuthSuccessHandler.class);
    private final UserService userService;
    private final JWTUtil jwtUtil;
    private final String frontendBaseUrl;

    public OAuthSuccessHandler(
            UserService userService,
            JWTUtil jwtUtil,
            @Value("${app.frontend-url:http://localhost:5173}") String frontendBaseUrl
    ) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.frontendBaseUrl = frontendBaseUrl;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        try {
            OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

            String email = oauthUser.getAttribute("email");
            String name = oauthUser.getAttribute("name");
            log.info("OAuth success callback received: email={}, name={}", email, name);

            if (email == null) {
                throw new RuntimeException("Email not found from OAuth provider");
            }

            User user = userService.findOrCreate(email);
            log.info("OAuth user resolved: {}", user.getEmail());

            String token = jwtUtil.generateToken(user.getEmail());
            log.info("JWT generated for OAuth user");

            String redirectUrl = UriComponentsBuilder
                    .fromUriString(frontendBaseUrl)
                    .path("/")
                    .queryParam("token", token)
                    .build(true)
                    .toUriString();

            log.info("OAuth redirecting to frontend root");

            response.sendRedirect(redirectUrl);

        } catch (Exception e) {
            log.error(
                    "OAuth success handler failed: uri={}, error={}",
                    request.getRequestURI(),
                    e.getMessage(),
                    e
            );
            String redirectUrl = UriComponentsBuilder
                    .fromUriString(frontendBaseUrl)
                    .path("/")
                    .queryParam("error", "oauth")
                    .queryParam("reason", "success_handler_exception")
                    .build(true)
                    .toUriString();
            response.sendRedirect(redirectUrl);
        }
    }
}
