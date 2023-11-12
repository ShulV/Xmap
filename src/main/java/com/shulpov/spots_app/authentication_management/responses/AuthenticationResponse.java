package com.shulpov.spots_app.authentication_management.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    @Schema(description = "Id пользователя", example = "1")
    @JsonProperty("id")
    private Long userId;

    @Schema(description = "Access-токен пользователя",
            example = "eyJhbGciOiJIUzI1NiJ9.eyJhdXRob3JpdGllcyI6W3siYXV0aG9yaXR5IjoiUk9MRV9VU0VSIn1dLCJjcmVhdGluZ0Rhd" +
                    "GUiOjE2OTkyNTU3NzUyMzQsInVzZXJuYW1lIjoiYWxleF9ncmVlbkBnbWFpbC5jb20iLCJzdWIiOiJhbGV4X2dyZWVuQGdtY" +
                    "WlsLmNvbSIsImlhdCI6MTY5OTI1NTc3NSwiZXhwIjoxNjk5MzQyMTc1fQ.km9y4BK3I1p7c4FbqSOmYzbGAIo-W_jHOAgY_MUKkAg")
    @JsonProperty("access_token")
    private String accessToken;

    @Schema(description = "Refresh-токен пользователя",
            example = "eyJhbGciOiJIUzI1NiJ9.eyJjcmVhdGluZ0RhdGUiOjE2OTkyNTU3NzUyODAsInN1YiI6ImFsZXhfZ3JlZW5AZ21haWwuY" +
                    "29tIiwiaWF0IjoxNjk5MjU1Nzc1LCJleHAiOjE2OTk4NjA1NzV9.AErZPqLTbVykO11Ro16c_BmvSiCPJuglNRpcPybi7sY")
    @JsonProperty("refresh_token")
    private String refreshToken;
}
