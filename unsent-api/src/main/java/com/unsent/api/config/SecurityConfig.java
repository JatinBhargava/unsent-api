package com.unsent.api.config;

import com.unsent.api.Util.OAuthFailureHandler;
import com.unsent.api.Util.OAuthSuccessHandler;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    OAuthSuccessHandler authSuccessHandler;
    @Autowired
    OAuthFailureHandler authFailureHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())

                .exceptionHandling(ex ->
                        ex.authenticationEntryPoint(
                                (req, res,
                                 authEx) -> {
                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }))

                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/auth/**", "/oauth2/**", "/login/**", "/error", "/**")
                                .permitAll().anyRequest().authenticated())

                // OAuth ONLY when explicitly called
                .oauth2Login(oauth ->
                        oauth
                                .successHandler(authSuccessHandler)
                                .failureHandler(authFailureHandler));

        return http.build();
    }
}
