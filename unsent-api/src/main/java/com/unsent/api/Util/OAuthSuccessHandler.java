package com.unsent.api.Util;

import com.unsent.api.config.JWTUtil;
import com.unsent.api.entity.User;
import com.unsent.api.service.AuthService;
import com.unsent.api.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    UserService userService;

    @Autowired
    JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        OAuth2User user = (OAuth2User) authentication.getPrincipal();
        String email = user.getAttribute("email");

        // 1️⃣ Find or create user in DB
        User dbUser = userService.findOrCreate(email);

        // 2️⃣ Generate JWT
        String token = jwtUtil.generateToken(String.valueOf(dbUser));

        // 3️⃣ Set HttpOnly cookie
        ResponseCookie cookie = ResponseCookie.from("SESSION", token)
                .httpOnly(true)
                .secure(false) // true in prod
                .path("/")
                .sameSite("Lax")
                .maxAge(Duration.ofDays(7))
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        // 4️⃣ Redirect to frontend
        response.sendRedirect("http://localhost:5174/app");
    }
}