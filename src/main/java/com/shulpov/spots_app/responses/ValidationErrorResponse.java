package com.shulpov.spots_app.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shulpov.spots_app.dto.FieldErrorDto;
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
    @JsonProperty("errors")
    private List<FieldErrorDto> errorDtoList;
}