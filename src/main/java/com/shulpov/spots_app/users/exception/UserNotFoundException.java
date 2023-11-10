package com.shulpov.spots_app.users.exception;

/**
 * Исключение того, что пользователь не найден
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
