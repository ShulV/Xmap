package com.shulpov.spots_app.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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
public class FieldErrorDto {
    @Schema(description = "Сообщение об ошибке", example = "Email должен быть валидным")
    @JsonProperty("error_message")
    private String defaultMessage;

    @Schema(description = "Название поля, на котором вызвана ошибка", example = "email")
    @JsonProperty("field")
    private String field;

    @Schema(description = "Переданное неверное значение поля", example = "kek.mail.ru")
    @JsonProperty("rejected_value")
    private String rejectedValue;
}
