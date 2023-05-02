package com.shulpov.spots_app.config;

import com.shulpov.spots_app.services.AppUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final AppUserDetailsService appUserDetailsService;
//    private final JWTFilter jwtFilter;

    public SecurityConfig(AppUserDetailsService appUserDetailsService) {
        this.appUserDetailsService = appUserDetailsService;
//        this.jwtFilter = jwtFilter;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                //отключаем межасайтовую подделку форм, т.к. у нас REST API и здесь такой проблемы нет
                .csrf().disable()
//                .authorizeHttpRequests()
                //разрешить неаутентифицир. пользователям обращаться
//                .requestMatchers("/api/auth/register", "/error").permitAll()
//                .and()
                //не сохранять сессии автоматически (т.к. мы используем JWT)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //фильтр для проверки токенов всех запросов
//                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                //
                .userDetailsService(appUserDetailsService)
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

