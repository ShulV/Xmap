package com.shulpov.spots_app.auth.services;

import com.shulpov.spots_app.auth.exceptions.RegisterErrorException;
import com.shulpov.spots_app.auth.requests.AuthenticationRequest;
import com.shulpov.spots_app.auth.requests.RegisterRequest;
import com.shulpov.spots_app.auth.responses.AuthenticationResponse;
import com.shulpov.spots_app.auth.responses.RegisterResponse;
import com.shulpov.spots_app.auth.token.Token;
import com.shulpov.spots_app.auth.token.TokenRepository;
import com.shulpov.spots_app.auth.token.TokenType;
import com.shulpov.spots_app.user.Role;
import com.shulpov.spots_app.user.User;
import com.shulpov.spots_app.user.UserRepository;
import com.shulpov.spots_app.auth.validators.UserValidator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.Date;

/**
 * @author Shulpov Victor
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserValidator userValidator;

    /**
     * Регистрация пользователя (с валидацией данных пользователя, с генерацией токенов для пользователя)
     * @param request данные пользователя, указанные при регистрации
     * @param errors ошибки валидации
     * @return RegisterResponse
     */
    @Transactional
    public RegisterResponse register(RegisterRequest request, BindingResult errors) {
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .birthday(request.getBirthday())
                .passHash(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .regDate(new Date())
                .build();
        userValidator.validate(user, errors);
        if(errors.hasErrors()) {
            throw new RegisterErrorException("Регистрация не удалась.", errors.getFieldErrors());
        }
        User savedUser = userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        //todo СОХРАНЯТЬ НАДО refreshToken, подумать над проверкой access'а (сделать иначе)
        saveUserToken(savedUser, jwtToken);
        return RegisterResponse.builder()
                .userId(user.getId())
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * Аутентификация пользователя по логину (почте) и паролю
     * @param request учетные данные (логин и пароль)
     * @return AuthenticationResponse
     */
    @Transactional
    public AuthenticationResponse authenticate(AuthenticationRequest request) throws BadCredentialsException {
        //throws BadCredentialsException
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        //authenticated
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .userId(user.getId())
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * //todo
     * @param user
     * @param jwtToken
     */
    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    /**
     * //todo
     * @param user
     */
    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    /**
     * //todo
     * @param request
     * @return
     * @throws AuthenticationCredentialsNotFoundException
     */
    public ResponseEntity<AuthenticationResponse> refreshToken(
            HttpServletRequest request
    ) throws AuthenticationCredentialsNotFoundException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            throw new AuthenticationCredentialsNotFoundException("Bearer token not found");
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractEmail(refreshToken);
        if (userEmail != null) {
            var user = userRepository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                //todo это перенесется в кастомный response, который будет возвращаться в зависимости от ошибки
                AuthenticationResponse authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                return ResponseEntity.ok(authResponse);
            }
            throw new AuthenticationCredentialsNotFoundException("Token is not valid");
        }
        throw new AuthenticationCredentialsNotFoundException("Email in token not found");
    }
}

