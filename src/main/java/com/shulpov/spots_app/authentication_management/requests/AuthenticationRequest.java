package com.shulpov.spots_app.authentication_management.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    @NotEmpty
    @Schema(description = "Логин (почта пользователя)", example = "alex_green@gmail.com")
    private String email;

    @NotEmpty
    @Schema(description = "Пароль пользователя", example = "password")
    private String  password;
}
