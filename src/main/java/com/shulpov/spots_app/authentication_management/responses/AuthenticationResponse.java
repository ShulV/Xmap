package com.shulpov.spots_app.authentication_management.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * @author Shulpov Victor
 */

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    @JsonProperty("id")
    private Long userId;
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;
}
