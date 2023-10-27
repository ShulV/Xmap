package com.shulpov.spots_app.auth.services;

import com.shulpov.spots_app.auth.exceptions.RegisterErrorException;
import com.shulpov.spots_app.auth.requests.AuthenticationRequest;
import com.shulpov.spots_app.auth.requests.RegisterRequest;
import com.shulpov.spots_app.auth.responses.AuthenticationResponse;
import com.shulpov.spots_app.auth.responses.LogoutMessageResponse;
import com.shulpov.spots_app.auth.responses.RegisterResponse;
import com.shulpov.spots_app.auth.token.Token;
import com.shulpov.spots_app.auth.token.TokenRepository;
import com.shulpov.spots_app.auth.token.TokenService;
import com.shulpov.spots_app.auth.token.TokenType;
import com.shulpov.spots_app.auth.validators.UserValidator;
import com.shulpov.spots_app.user.Role;
import com.shulpov.spots_app.user.User;
import com.shulpov.spots_app.user.UserRepository;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import javax.naming.AuthenticationException;
import java.util.Date;
import java.util.Optional;

/**
 * @author Shulpov Victor
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final UserValidator userValidator;

    /**
     * Регистрация пользователя (с валидацией данных пользователя, с генерацией токенов для пользователя)
     * @param request данные пользователя, указанные при регистрации
     * @param errors ошибки валидации
     * @return RegisterResponse
     */
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
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, refreshToken, TokenType.REFRESH);
        return RegisterResponse.builder()
                .userId(user.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * Аутентификация пользователя по логину (почте) и паролю
     * @param request учетные данные (логин и пароль)
     * @return AuthenticationResponse
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) throws BadCredentialsException {
        String email = request.getEmail();
        //throws BadCredentialsException
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        request.getPassword()
                )
        );
        //authenticated
        Optional<User> userOpt = userRepository.findByEmail(email);
        if(userOpt.isEmpty()) {
            throw new BadCredentialsException("User with email='" + email + "' not found");
        }
        User user = userOpt.get();
        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(user, newRefreshToken, TokenType.REFRESH);
        return createAuthResponse(user.getId(), newAccessToken, newRefreshToken);
    }

    /**
     * Сохранить токен пользователя в БД
     * @param user пользователь
     * @param jwtToken токен
     * @param tokenType тип токена
     */
    private void saveUserToken(User user, String jwtToken, TokenType tokenType) {
        var token = Token.builder()
                .user(user)
                .value(jwtToken)
                .tokenType(tokenType)
                .build();
        tokenRepository.save(token);
    }

    /**
     * //TODO
     * @param authToken
     * @return
     * @throws AuthenticationException
     */
    private Token validateRefreshToken(String authToken) throws AuthenticationException {
        final String oldRefreshToken;
        //заголовка с refresh токеном нет
        if (authToken == null || !authToken.startsWith("Refresh ")) {
            throw new AuthenticationException("Refresh token not found in headers");
        }
        oldRefreshToken = authToken.substring(8);

        try {
            //проверка: не протух ли токен
            if(jwtService.isTokenExpired(oldRefreshToken)) {
                throw new AuthenticationException("Refresh is expired");
            }
        } catch (JwtException e) {
            throw new AuthenticationException("JWT token error");
        }

        //проверка: есть ли refresh токен в базе
        Optional<Token> refreshTokenFromDB = tokenService.getTokenByValue(oldRefreshToken);
        if(refreshTokenFromDB.isEmpty()) {
            throw new AuthenticationException("Refresh not found in DB");
        }
        Token refreshToken = refreshTokenFromDB.get();
        if(refreshToken.getUser() == null) {
            throw new AuthenticationException("Token without user");//такого случаться вообще не должно!
        }
        return refreshToken;
    }

    /**
     * //TODO
     * @param request
     * @return
     * @throws AuthenticationException
     */
    public AuthenticationResponse refreshToken(HttpServletRequest request) throws AuthenticationException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        Token refreshToken = validateRefreshToken(authHeader);
        User user = refreshToken.getUser();
        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);
        refreshToken.setValue(newRefreshToken);
        tokenService.save(refreshToken);
        return createAuthResponse(user.getId(), newAccessToken, newRefreshToken);
    }

    /**
     * Создать объект ответа успешной аутентификации
     * @param userId id пользователя
     * @param accessToken краткосрочный токен для аутентификации запросов
     * @param refreshToken долгосрочный токен для обновления токенов
     * @return новые access и refresh токены, также id пользователя
     */
    private AuthenticationResponse createAuthResponse(Long userId, String accessToken, String refreshToken) {
        return AuthenticationResponse.builder()
                .userId(userId)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * //TODO
     * @param refreshToken
     * @return
     */
    public LogoutMessageResponse logout(String refreshToken) throws AuthenticationException {
        Token token = validateRefreshToken(refreshToken);
        tokenService.deleteToken(token);
        return LogoutMessageResponse.builder()
                .closedSessionNumber(1L)
                .userId(token.getUser().getId())
                .message("Successful logout")
                .build();
    }

    /**
     * //TODO
     * @param refreshToken
     * @return
     */
    public LogoutMessageResponse logoutAll(String refreshToken) throws AuthenticationException {
        Token token = validateRefreshToken(refreshToken);
        long count = tokenService.count();
        tokenService.deleteAllTokens(token.getUser().getTokens());
        return LogoutMessageResponse.builder()
                .closedSessionNumber(count)
                .userId(token.getUser().getId())
                .message("Successful logout")
                .build();
    }
}

