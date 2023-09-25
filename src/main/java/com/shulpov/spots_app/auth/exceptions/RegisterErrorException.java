package com.shulpov.spots_app.auth.exceptions;

import lombok.Getter;
import org.springframework.validation.FieldError;

import java.util.List;

/**
 * @author Shulpov Victor
 */
@Getter
public class RegisterErrorException extends RuntimeException {

    private List<FieldError> errors;

    public RegisterErrorException(String message, List<FieldError> errors) {
        super(message);
        this.errors = errors;
    }

    public void setErrors(List<FieldError> errors) {
        this.errors = errors;
    }
}
