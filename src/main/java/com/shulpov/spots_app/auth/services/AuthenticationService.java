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
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

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
     * Проверить валидность refresh-токена
     * @param tokenHeader значение заголовка Authorization (Refresh tokenValue)
     * @return refresh-токен из БД
     */
    private Token validateRefreshToken(String tokenHeader) throws JwtException {
        final String oldRefreshToken;
        //заголовка с refresh-токеном нет
        if (tokenHeader == null || !tokenHeader.startsWith("Refresh ")) {
            throw new JwtException("Refresh token not found in headers");
        }
        oldRefreshToken = tokenHeader.substring(8);

        try {
            //проверка: не протух ли токен
            if(jwtService.isTokenExpired(oldRefreshToken)) {
                throw new JwtException("Refresh is expired");
            }
        } catch (JwtException e) {
            throw new JwtException("JWT token error");
        }

        //проверка: есть ли refresh токен в базе
        Optional<Token> refreshTokenFromDB = tokenService.getTokenByValue(oldRefreshToken);
        if(refreshTokenFromDB.isEmpty()) {
            throw new JwtException("Refresh not found in DB");
        }
        Token refreshToken = refreshTokenFromDB.get();
        if(refreshToken.getUser() == null) {
            throw new JwtException("Token without user");//такого случаться вообще не должно!
        }
        return refreshToken;
    }

    /**
     * Получить новые access и refresh токены по старому refresh-токену
     * @param refreshTokenHeader значение заголовка Authorization (Refresh tokenValue)
     */
    public AuthenticationResponse refreshToken(String refreshTokenHeader) throws JwtException {
        Token refreshToken = validateRefreshToken(refreshTokenHeader);
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
     * Выйти из учетной записи
     * @param refreshTokenHeader значение заголовка Authorization (Refresh tokenValue)
     */
    public LogoutMessageResponse logout(String refreshTokenHeader) throws JwtException {
        Token token = validateRefreshToken(refreshTokenHeader);
        tokenService.deleteToken(token);
        return LogoutMessageResponse.builder()
                .closedSessionNumber(1L)
                .userId(token.getUser().getId())
                .message("Successful logout")
                .build();
    }

    /**
     * Выйти изо всех учетных записей
     * @param refreshTokenHeader значение заголовка Authorization (Refresh tokenValue)
     */
    public LogoutMessageResponse logoutAll(String refreshTokenHeader) throws JwtException {
        Token token = validateRefreshToken(refreshTokenHeader);
        long count = tokenService.count();
        tokenService.deleteAllTokens(token.getUser().getTokens());
        return LogoutMessageResponse.builder()
                .closedSessionNumber(count)
                .userId(token.getUser().getId())
                .message("Successful logout")
                .build();
    }
}

