package com.shulpov.spots_app.auth.responses;

import com.shulpov.spots_app.responses.ValidationErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterErrorResponse extends ValidationErrorResponse {
    private String message;
}
