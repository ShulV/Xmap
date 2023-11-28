package com.shulpov.spots_app.common;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Универсальная обработка всех неотловленных исключений (сгенерил chat-gpt)
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final Logger logger;

    // Обработка ошибок валидации данных
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
        logger.atError().log("Unhandled exception. Validation failed: [e.message = '{}' ]", ex.getMessage());
        return new ResponseEntity<>("Validation failed: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // Обработка ошибок авторизации
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        logger.atError().log("Unhandled exception. Access denied: [e.message = '{}' ]", ex.getMessage());
        return new ResponseEntity<>("Access denied: " + ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    // Обработка ошибок аутентификации
    @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class, LockedException.class})
    public ResponseEntity<String> handleAuthenticationException(Exception ex) {
        logger.atError().log("Unhandled exception. Authentication failed: [e.message = '{}' ]", ex.getMessage());
        return new ResponseEntity<>("Authentication failed: " + ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    // Обработка ошибок отсутствия ресурса
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
        logger.atError().log("Unhandled exception. Entity not found: [e.message = '{}' ]", ex.getMessage());
        return new ResponseEntity<>("Entity not found: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // Обработка ошибок конфликта
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        logger.atError().log("Unhandled exception. Data integrity violation: [e.message = '{}' ]", ex.getMessage());
        return new ResponseEntity<>("Data integrity violation: " + ex.getMessage(), HttpStatus.CONFLICT);
    }

    // Обработка ошибок обработки данных
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        logger.atError().log("Unhandled exception. Error processing request: [e.message = '{}' ]", ex.getMessage());
        return new ResponseEntity<>("Error processing request: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // Обработка ошибок внутреннего сервера
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleInternalServerException(Exception ex) {
        logger.atError().log("Unhandled exception. Internal server error: [e.message = '{}' ]", ex.getMessage());
        return new ResponseEntity<>("Internal server error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Обработка ошибок типа MethodArgumentTypeMismatchException (например, при передаче строки вместо числа)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        logger.atError().log("Unhandled exception. Type mismatch: [e.message = '{}' ]", ex.getMessage());
        return new ResponseEntity<>("Type mismatch: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}