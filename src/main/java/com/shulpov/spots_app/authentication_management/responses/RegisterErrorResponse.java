package com.shulpov.spots_app.authentication_management.responses;

import com.shulpov.spots_app.responses.ValidationErrorResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterErrorResponse extends ValidationErrorResponse {
    @Schema(description = "Сообщение о ошибке", example = "Регистрация не удалась")
    private String message;
}
