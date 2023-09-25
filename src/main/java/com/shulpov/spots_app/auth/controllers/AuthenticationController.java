package com.shulpov.spots_app.auth.controllers;

import com.shulpov.spots_app.auth.exceptions.RegisterErrorException;
import com.shulpov.spots_app.auth.requests.AuthenticationRequest;
import com.shulpov.spots_app.auth.requests.RegisterRequest;
import com.shulpov.spots_app.auth.responses.AuthenticationResponse;
import com.shulpov.spots_app.auth.responses.RegisterErrorResponse;
import com.shulpov.spots_app.auth.responses.RegisterResponse;
import com.shulpov.spots_app.auth.services.AuthenticationService;
import com.shulpov.spots_app.dto.FieldErrorDto;
import com.shulpov.spots_app.responses.ErrorMessageResponse;
import com.shulpov.spots_app.utils.DtoConverter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @author Shulpov Victor
 */

@RestController
@RequestMapping(value = "/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    @Autowired
    private final AuthenticationService service;

    @Autowired
    private final DtoConverter dtoConverter;

    /**
     * Регистрация пользователя
     * @param request данные пользователя, необходимые для регистрации (JSON)
     * @param errors ошибки валидации (передавать их не нужно)
     * @return RegisterResponse (userId, accessToken, refreshToken)
     */
    @PostMapping(value="/register")
    public ResponseEntity<RegisterResponse> register(
            @Valid @RequestBody RegisterRequest request, BindingResult errors
    ) {
        RegisterResponse response = service.register(request, errors);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
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
     * Аутентификация по паролю и логину
     * @param request пароль и логин пользователя (JSON)
     * @return AuthenticationResponse (userId, accessToken, refreshToken)
     */
    @PostMapping(value="/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    /**
     * Обработчик ошибки аутентификации (для неверных логина или пароля)
     * @param e исключение, содержащее текст ошибки
     * @return ErrorMessageResponse
     */
    @ExceptionHandler
    private ResponseEntity<ErrorMessageResponse> handleBadCredentialsException(BadCredentialsException e) {
        ErrorMessageResponse response = new ErrorMessageResponse();
        response.setErrorMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @PostMapping(value="/refresh-token", produces = "application/json")
    public ResponseEntity<?> refreshToken(
            HttpServletRequest request
    ) {
        try {
            return service.refreshToken(request);
        } catch (AuthenticationCredentialsNotFoundException | NoSuchElementException | BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("errorMessage", e.getMessage()));
        }
    }

}
