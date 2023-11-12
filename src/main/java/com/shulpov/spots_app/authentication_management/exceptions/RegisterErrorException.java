package com.shulpov.spots_app.authentication_management.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.FieldError;

import java.util.List;

/**
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
@Getter
@Setter
public class RegisterErrorException extends RuntimeException {

    private List<FieldError> errors;

    public RegisterErrorException(String message, List<FieldError> errors) {
        super(message);
        this.errors = errors;
    }
}
