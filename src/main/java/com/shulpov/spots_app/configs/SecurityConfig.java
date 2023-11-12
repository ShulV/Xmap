package com.shulpov.spots_app.configs;

import com.shulpov.spots_app.authentication_management.auth_providers.LoginPasswordAuthenticationProvider;
import com.shulpov.spots_app.authentication_management.filters.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final LoginPasswordAuthenticationProvider loginPasswordAuthenticationProvider;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                          LoginPasswordAuthenticationProvider loginPasswordAuthenticationProvider) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.loginPasswordAuthenticationProvider = loginPasswordAuthenticationProvider;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                //отключаем межасайтовую подделку форм, т.к. у нас REST API и здесь такой проблемы нет
                .csrf().disable()
                .authorizeHttpRequests()
                //разрешить неаутентифицир. пользователям обращаться
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                .requestMatchers(
                        "/api/v1/auth/**",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/error").permitAll()
                .requestMatchers(HttpMethod.GET,
                        "/api/v1/spots/all",
                        "/api/v1/spot-types/**",
                        "/api/v1/sport-types/**",
                        "/api/v1/space-types/**",
                        "/api/v1/spots/in-radius",
                        "/api/v1/countries/**",
                        "/api/v1/cities/**",
                        "/api/v1/regions/**",
                        "/api/v1/comments/get-by-spot-id/**",
                        "/api/v1/spots-users/like-number/**",
                        "/api/v1/spots-users/favorite-number/**",
                        "/api/v1/spots-users/info/**",
                        "/api/v1/image-service/user-image/**",
                        "/api/v1/image-service/spot-image/**").permitAll()
                .anyRequest().hasAnyRole("USER", "MODERATOR", "ADMIN")
                .and()
                    //не сохранять сессии автоматически (т.к. мы используем JWT)
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .authenticationProvider(loginPasswordAuthenticationProvider)
                    //фильтр для проверки токенов всех запросов
                    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}

