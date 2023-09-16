package com.shulpov.spots_app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class FieldErrorDto {
    @JsonProperty("error_message")
    private String defaultMessage;
    @JsonProperty("field")
    private String field;
    @JsonProperty("rejected_value")
    private String rejectedValue;
}
