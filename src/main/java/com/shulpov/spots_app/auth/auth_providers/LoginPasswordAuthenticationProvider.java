package com.shulpov.spots_app.auth.auth_providers;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * @author Shulpov Victor
 */

public class LoginPasswordAuthenticationProvider implements AuthenticationProvider {
    /**
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //todo
        return null;
    }

    /**
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {

        //todo
        return false;
    }
}
