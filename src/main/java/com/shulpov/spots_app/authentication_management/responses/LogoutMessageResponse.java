package com.shulpov.spots_app.authentication_management.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogoutMessageResponse {
    @Schema(description = "Сообщение о выходе из аккаунта", example = "Successful logout")
    private String message;

    @Schema(description = "Id пользователя", example = "1")
    @JsonProperty("id")
    private Long userId;

    @Schema(description = "Количество закрытых сессий", example = "1")
    @JsonProperty("closed_session_number")
    private long closedSessionNumber;
}
