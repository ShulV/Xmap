package com.shulpov.spots_app.users.exception;

/**
 * Исключение того, что пользователь не найден
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
