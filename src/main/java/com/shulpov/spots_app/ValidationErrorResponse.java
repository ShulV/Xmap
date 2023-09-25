package com.shulpov.spots_app;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shulpov.spots_app.dto.FieldErrorDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.FieldError;

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
    List<FieldErrorDto> errorDtoList;
}