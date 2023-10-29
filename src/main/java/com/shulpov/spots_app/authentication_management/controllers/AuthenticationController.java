package com.shulpov.spots_app.authentication_management.controllers;

import com.shulpov.spots_app.authentication_management.exceptions.RegisterErrorException;
import com.shulpov.spots_app.authentication_management.requests.AuthenticationRequest;
import com.shulpov.spots_app.authentication_management.requests.RegisterRequest;
import com.shulpov.spots_app.authentication_management.responses.AuthenticationResponse;
import com.shulpov.spots_app.authentication_management.responses.LogoutMessageResponse;
import com.shulpov.spots_app.authentication_management.responses.RegisterErrorResponse;
import com.shulpov.spots_app.authentication_management.responses.RegisterResponse;
import com.shulpov.spots_app.authentication_management.services.AuthenticationService;
import com.shulpov.spots_app.dto.FieldErrorDto;
import com.shulpov.spots_app.responses.ErrorMessageResponse;
import com.shulpov.spots_app.utils.DtoConverter;
import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Shulpov Victor
 */

@RestController
@RequestMapping(value = "/api/v1/auth")
@Tag(name="Контроллер управления аутентификацией пользователей")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    private final DtoConverter dtoConverter;

    @Operation(
            summary = "Регистрация",
            description = "Зарегистрироваться (с проверкой данных на валидность)."
    )
    @PostMapping(value="/register")
    public ResponseEntity<RegisterResponse> register(
            @Parameter(description = "Объект с данными пользователя, необходимыми для регистрации", required = true)
            @Valid @RequestBody RegisterRequest request,
            @Parameter(description = "Ошибки валидации для ответа (передавать их не нужно)", hidden = true)
            BindingResult errors
    ) {
        RegisterResponse response = service.register(request, errors);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Аутентификация",
            description = "Аутентифицироваться по логину и паролю"
    )
    @PostMapping(value="/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @Parameter(description = "Объект с логином и паролем пользователя", required = true)
            @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @Operation(
            summary = "Получение новых токенов",
            description = "Получение новых access и refresh токенов пользователя"
    )
    @PostMapping(value="/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            @Parameter(description = "Refresh-токен (JWT). Передавать в виде: 'Refresh tokenValue'", required = true)
            @RequestHeader(value = "Authorization") String refreshToken)
            throws JwtException {
        return ResponseEntity.status(HttpStatus.OK).body(service.refreshToken(refreshToken));
    }

    @Operation(
            summary = "Выход из учетной записи",
            description = "Выход из текущей (одной) учетной записи пользователя"
    )
    @DeleteMapping(value = "/logout")
    public ResponseEntity<LogoutMessageResponse> logout(
            @Parameter(description = "Refresh-токен (JWT). Передавать в виде: 'Refresh tokenValue'", required = true)
            @RequestHeader(value = "Authorization") String refreshToken)
            throws JwtException {
        return ResponseEntity.status(HttpStatus.OK).body(service.logout(refreshToken));
    }

    @Operation(
            summary = "Выход из всех учетных записей",
            description = "Выход из всех учетных записей пользователя"
    )
    @DeleteMapping(value = "/logout-all")
    public ResponseEntity<LogoutMessageResponse> logoutAll(
            @Parameter(description = "Refresh-токен (JWT). Передавать в виде: 'Refresh tokenValue'", required = true)
            @RequestHeader(value = "Authorization") String refreshToken)
            throws JwtException {
        return ResponseEntity.status(HttpStatus.OK).body(service.logoutAll(refreshToken));
    }

    /**
     * Обработчик ошибки регистрации
     * @param e исключение, содержащее текст ошибки и подробное описание ошибок полей
     * @return RegisterErrorResponse
     */
    @ExceptionHandler
    private ResponseEntity<RegisterErrorResponse> handleRegisterErrorException(RegisterErrorException e) {
        RegisterErrorResponse response = new RegisterErrorResponse(e.getMessage());
        List<FieldErrorDto> errorDtoList = e.getErrors().stream().map(dtoConverter::fieldErrorToDto).toList();
        response.setErrorDtoList(errorDtoList);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Обработчик ошибки аутентификации (для неверных логина или пароля)
     * @param e исключение, содержащее текст ошибки
     * @return ответ с сообщением об ошибке
     */
    @ExceptionHandler
    private ResponseEntity<ErrorMessageResponse> handleBadCredentialsException(BadCredentialsException e) {
        ErrorMessageResponse response = new ErrorMessageResponse();
        response.setErrorMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    /**
     * Обработчик проблем, связанных с JWT-токенами
     * @param e исключение, содержащее текст ошибки
     * @return ответ с сообщением об ошибке
     */
    @ExceptionHandler
    private ResponseEntity<ErrorMessageResponse> handleJwtExceptionException(JwtException e) {
        ErrorMessageResponse response = new ErrorMessageResponse();
        response.setErrorMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}
