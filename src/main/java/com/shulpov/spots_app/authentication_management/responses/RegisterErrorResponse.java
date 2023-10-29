package com.shulpov.spots_app.authentication_management.responses;

import com.shulpov.spots_app.responses.ValidationErrorResponse;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterErrorResponse extends ValidationErrorResponse {
    private String message;
}
