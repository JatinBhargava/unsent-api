package com.unsent.api.Util;

import com.unsent.api.config.JWTUtil;
import com.unsent.api.entity.User;
import com.unsent.api.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {

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
            String name  = oauthUser.getAttribute("name");

            System.out.println("OAuth email: " + email);

            if (email == null) {
                throw new RuntimeException("Email not found from OAuth provider");
            }

            // 1️⃣ Find or create user
            User user = userService.findOrCreate(email);

            System.out.println("User found/created: " + user.getEmail());

            // 2️⃣ Generate JWT
            String token = jwtUtil.generateToken(user.getEmail());

            System.out.println("JWT generated");

            // 3️⃣ Redirect
            String redirectUrl = UriComponentsBuilder
                    .fromUriString(frontendBaseUrl)
                    .path("/")
                    .queryParam("token", token)
                    .build(true)
                    .toUriString();

            System.out.println("Redirecting to: " + redirectUrl);

            response.sendRedirect(redirectUrl);

        } catch (Exception e) {
            e.printStackTrace();
            String redirectUrl = UriComponentsBuilder
                    .fromUriString(frontendBaseUrl)
                    .path("/")
                    .queryParam("error", "oauth")
                    .build(true)
                    .toUriString();
            response.sendRedirect(redirectUrl);
        }
    }
}
