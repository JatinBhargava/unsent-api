package com.unsent.api.Util;

import com.unsent.api.config.JWTUtil;
import com.unsent.api.entity.User;
import com.unsent.api.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;

@Component
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;
    private final JWTUtil jwtUtil;

    public OAuthSuccessHandler(UserService userService, JWTUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
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

        // 3️⃣ HttpOnly cookie (PROD SAFE)
        ResponseCookie cookie = ResponseCookie.from("SESSION", token)
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")    // MUST be true on Render (HTTPS)
                .path("/")
                .sameSite("None") // Required for cross-domain OAuth
                .maxAge(Duration.ofDays(7))
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        // 4️⃣ Redirect to frontend
        response.sendRedirect("http://localhost:5173/diaries");
    }
}