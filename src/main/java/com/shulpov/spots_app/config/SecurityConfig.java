package com.shulpov.spots_app.config;

import com.shulpov.spots_app.services.PersonDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final PersonDetailsService personDetailsService;
    private final JWTFilter jwtFilter;

    public SecurityConfig(PersonDetailsService personDetailsService, JWTFilter jwtFilter) {
        this.personDetailsService = personDetailsService;
        this.jwtFilter = jwtFilter;
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
                        "/api/auth/login",
                        "/api/auth/register",
                        "/api/spot-types/**",
                        "/api/sport-types/**",
                        "/api/space-types/**",
                        "/api/spots/get-all",
                        "/api/image-service/download-spot-image/**",
                        "/moderator/auth/**",
                        "/css/**",
                        "/js/**",
                        "/error").permitAll()
                .anyRequest().hasAnyRole("USER", "MODERATOR", "ADMIN")
                .and()
                //не сохранять сессии автоматически (т.к. мы используем JWT)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //фильтр для проверки токенов всех запросов
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                //
                .userDetailsService(personDetailsService)
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}

