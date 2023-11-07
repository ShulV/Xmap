package com.shulpov.spots_app.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shulpov.spots_app.dto.FieldErrorDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

/**
 * @author Shulpov Victor
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