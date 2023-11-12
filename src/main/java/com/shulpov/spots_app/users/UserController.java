package com.shulpov.spots_app.users;

import com.shulpov.spots_app.common.responses.ErrorMessageResponse;
import com.shulpov.spots_app.users.dto.MainUserInfoDto;
import com.shulpov.spots_app.users.exception.UserNotFoundException;
import com.shulpov.spots_app.users.services.UserService;
import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.*;

/**
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/user")
@Tag(name="Контроллер пользователя", description="Позволяет получать информацию о пользователе и удалять аккаунт")
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Получение полной информации о пользователе",
            description = "Позволяет получить полную информацию о пользователе",
            security = @SecurityRequirement(name = "accessTokenAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Данные пользователя успешно получены",
                            content = { @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = MainUserInfoDto.class)) }
                    ),
                    @ApiResponse(responseCode = "400", description = "Пользователь не был найден или получены ошибки JWT",
                            content = { @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessageResponse.class)) }
                    )}
    )
    @GetMapping("/info")
    public ResponseEntity<MainUserInfoDto> getAuthUser(
            @Parameter(description = "Access токен", example = "Bearer token_value")
            @RequestHeader("Authorization") String accessHeader) {
        return ResponseEntity.ok(userService.getMainInfoByAccessToken(accessHeader));
    }

    /**
     * Обработчик ошибки ненайденного пользователя или Jwt
     * @param e исключение, содержащее текст ошибки
     */
    @ExceptionHandler({UserNotFoundException.class, JwtException.class, AuthenticationCredentialsNotFoundException.class})
    private ResponseEntity<ErrorMessageResponse> handleJwtExceptionException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessageResponse(e.getMessage()));
    }
}
