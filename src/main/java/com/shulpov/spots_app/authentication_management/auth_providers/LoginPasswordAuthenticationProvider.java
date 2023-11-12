package com.shulpov.spots_app.authentication_management.auth_providers;

import com.shulpov.spots_app.users.models.User;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
@Component
public class LoginPasswordAuthenticationProvider implements AuthenticationProvider {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    private final Logger logger;

    @Autowired
    public LoginPasswordAuthenticationProvider(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService, Logger logger) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.logger = logger;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            User user = (User) userDetails;
            //user.getPassword возвращает хэш
            if(passwordEncoder.matches(password, user.getPassword())) {
                logger.info("Successful authenticated by LoginPasswordAuthenticationProvider: " +
                        "[user.id = '{}', user.email = '{}']", user.getId(), user.getUsername());
                return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
            }
            logger.info("Unsuccessful authenticated by LoginPasswordAuthenticationProvider (wrong password): " +
                    "[user.id = '{}', user.email = '{}']", user.getId(), user.getUsername());
            throw new BadCredentialsException("Неверный пароль");
        } catch (UsernameNotFoundException e) {
            logger.info("Unsuccessful authenticated by LoginPasswordAuthenticationProvider (wrong username)");
            throw new BadCredentialsException("Неверный логин");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
