package com.shulpov.spots_app.auth.services;

import com.shulpov.spots_app.auth.exceptions.RegisterErrorException;
import com.shulpov.spots_app.auth.requests.AuthenticationRequest;
import com.shulpov.spots_app.auth.requests.RegisterRequest;
import com.shulpov.spots_app.auth.responses.AuthenticationResponse;
import com.shulpov.spots_app.auth.responses.RegisterResponse;
import com.shulpov.spots_app.auth.token.Token;
import com.shulpov.spots_app.auth.token.TokenRepository;
import com.shulpov.spots_app.auth.token.TokenService;
import com.shulpov.spots_app.auth.token.TokenType;
import com.shulpov.spots_app.auth.validators.UserValidator;
import com.shulpov.spots_app.user.Role;
import com.shulpov.spots_app.user.User;
import com.shulpov.spots_app.user.UserRepository;
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
    @Transactional
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
     * Обновить токены с помощью долгосрочного refresh токена
     * @param request объект http-запроса
     * @return новые access и refresh токены, также id пользователя
     * @throws AuthenticationException ошибка аутентификации
     */
    public AuthenticationResponse refreshToken(
            HttpServletRequest request
    ) throws AuthenticationException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        //заголовка с refresh токеном нет
        if (authHeader == null || !authHeader.startsWith("Refresh ")) {
            throw new AuthenticationException("Refresh token not found");
        }
        refreshToken = authHeader.substring(8);

        //проверка: не протух ли токен
        if(jwtService.isTokenExpired(refreshToken)) {
            throw new AuthenticationException("Refresh is expired");
        }

        //проверка: есть ли refresh токен в базе
        Optional<Token> refreshTokenFromDB = tokenService.getTokenByValue(refreshToken);
        if(refreshTokenFromDB.isEmpty()) {
            throw new AuthenticationException("Refresh not found in DB");
        }

        final String userEmail = jwtService.extractEmail(refreshToken);
        if (userEmail != null) {
            Optional<User> userOpt = userRepository.findByEmail(userEmail);
            if(userOpt.isEmpty()) {
                throw new AuthenticationException("User with email='" + userEmail + "' from refresh token claim not found");
            }
            User user = userOpt.get();
            String newAccessToken = jwtService.generateAccessToken(user);
            String newRefreshToken = jwtService.generateRefreshToken(user);
            saveUserToken(user, newRefreshToken, TokenType.REFRESH);
            AuthenticationResponse response = createAuthResponse(user.getId(), newAccessToken, newRefreshToken);
            tokenService.deleteTokenByValue(refreshToken);
            return response;
        }
        throw new AuthenticationException("Email in refresh token claim is empty");
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
}

