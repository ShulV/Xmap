package com.shulpov.spots_app.responses;

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
public class ErrorMessageResponse {
    @JsonProperty("errorMessage")
    private String errorMessage;
}
