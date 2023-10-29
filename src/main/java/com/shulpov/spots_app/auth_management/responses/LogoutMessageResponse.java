package com.shulpov.spots_app.auth_management.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogoutMessageResponse {
    private String message;
    @JsonProperty("id")
    private Long userId;
    @JsonProperty("closed_session_number")
    private long closedSessionNumber;
}
