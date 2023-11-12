package com.shulpov.spots_app.common.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shulpov.spots_app.common.dto.FieldErrorDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

/**
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValidationErrorResponse {
    @Schema(description = "Массив ошибок, связанных с определенными полями")
    @JsonProperty("errors")
    private List<FieldErrorDto> errorDtoList;
}