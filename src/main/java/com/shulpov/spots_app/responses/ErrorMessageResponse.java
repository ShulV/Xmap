package com.shulpov.spots_app.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Shulpov Victor
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessageResponse {
    @Schema(description = "Сообщение об ошибке", example = "Пользователь с email='vova@mail.ru' не найден")
    @JsonProperty("errorMessage")
    private String errorMessage;
}
