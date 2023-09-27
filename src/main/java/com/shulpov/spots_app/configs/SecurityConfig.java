package com.shulpov.spots_app.configs;

import com.shulpov.spots_app.auth.auth_providers.LoginPasswordAuthenticationProvider;
import com.shulpov.spots_app.auth.filters.JwtAuthenticationFilter;
import com.shulpov.spots_app.auth.logout.CustomLogoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final LoginPasswordAuthenticationProvider loginPasswordAuthenticationProvider;
    private final CustomLogoutHandler customLogoutHandler;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                          LoginPasswordAuthenticationProvider loginPasswordAuthenticationProvider,
                          CustomLogoutHandler customLogoutHandler) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.loginPasswordAuthenticationProvider = loginPasswordAuthenticationProvider;
        this.customLogoutHandler = customLogoutHandler;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                //отключаем межасайтовую подделку форм, т.к. у нас REST API и здесь такой проблемы нет
                .csrf().disable()
                .authorizeHttpRequests()
                //разрешить неаутентифицир. пользователям обращаться
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers(
                        "/api/v1/auth/**",
                        "/api/spot-types/**",
                        "/api/sport-types/**",
                        "/api/space-types/**",
                        "/api/spots/get-all",
                        "/api/spots/get-in-radius",
                        "/api/countries/**",
                        "/api/cities/**",
                        "/api/regions/**",
                        "/api/comments/get-by-spot-id/**",
                        "/api/spots-users/get-like-number/**",
                        "/api/spots-users/get-favorite-number/**",
                        "/api/spots-users/get-info/**",
                        "/api/image-service/download-user-image/**",
                        "/api/image-service/download-spot-image/**",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/error").permitAll()
                .anyRequest().hasAnyRole("USER", "MODERATOR", "ADMIN")
                .and()
                    //не сохранять сессии автоматически (т.к. мы используем JWT)
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .authenticationProvider(loginPasswordAuthenticationProvider)
                    //фильтр для проверки токенов всех запросов
                    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                    //
                    .logout()
                    .logoutUrl("/api/v1/auth/logout")
                    .addLogoutHandler(customLogoutHandler)
                    .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                    //
                .and()
                .build();
    }
}

