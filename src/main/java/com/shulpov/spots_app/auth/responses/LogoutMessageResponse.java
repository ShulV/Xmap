package com.shulpov.spots_app.auth.responses;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogoutMessageResponse {
    private String message;
    private Long userId;
    private long closedSessionNumber;
}
