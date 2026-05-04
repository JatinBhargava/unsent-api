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
    private final String frontendLoginPath;

    public OAuthSuccessHandler(
            UserService userService,
            JWTUtil jwtUtil,
            @Value("${app.frontend-url:http://localhost:5173}") String frontendBaseUrl,
            @Value("${app.frontend-login-path:/}") String frontendLoginPath
    ) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.frontendBaseUrl = frontendBaseUrl;
        this.frontendLoginPath = frontendLoginPath;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");
        String name  = oauthUser.getAttribute("name");

        // 1️⃣ Find or create user
        User user = userService.findOrCreate(email);

        // 2️⃣ Generate JWT (email or userId ONLY)
        String token = jwtUtil.generateToken(user.getEmail());

        // 3️⃣ Redirect to frontend with token so SPA can finalize login
        String redirectUrl = UriComponentsBuilder
                .fromUriString(frontendBaseUrl)
                .path(frontendLoginPath)
                .queryParam("token", token)
                .build(true)
                .toUriString();

        response.sendRedirect(redirectUrl);
    }
}
