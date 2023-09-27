package com.shulpov.spots_app.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shulpov.spots_app.dto.FieldErrorDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Shulpov Victor
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValidationErrorResponse {
    @JsonProperty("errors")
    private List<FieldErrorDto> errorDtoList;
}