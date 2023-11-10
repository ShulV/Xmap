package com.shulpov.spots_app.users;

import com.shulpov.spots_app.responses.ErrorMessageResponse;
import com.shulpov.spots_app.users.dto.MainUserInfoDto;
import com.shulpov.spots_app.users.exception.UserNotFoundException;
import com.shulpov.spots_app.users.models.User;
import com.shulpov.spots_app.users.services.UserService;
import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
@Tag(name="Контроллер пользователя", description="Позволяет получать информацию о пользователе и удалять аккаунт")
public class UserController {
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
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
            @Parameter(description = "access token")
            @RequestHeader("Authorization") String accessToken) {
        return ResponseEntity.ok(userService.getMainInfoByAccessToken(accessToken));
    }

    @Operation(
            summary = "Удаление своего пользователя",
            description = "Позволяет удалить своего пользователя по токену",
            security = @SecurityRequirement(name = "accessTokenAuth")
    )
    @DeleteMapping("")
    public Map<String, Object> deleteUser() {
        logger.atInfo().log("DELETE /user");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        Optional<User> userOpt = userService.findByEmail(email);
        if(userOpt.isPresent()) {
            logger.atInfo().log("principle exists " +
                            "user: id={} name={} email={} phone={} birthday={} regDate={}",
                    userOpt.get().getId(),
                    userOpt.get().getName(),
                    userOpt.get().getEmail(),
                    userOpt.get().getPhoneNumber(),
                    userOpt.get().getBirthday(),
                    userOpt.get().getRegDate());
            Long id = userOpt.get().getId();
            if(Boolean.TRUE.equals(userService.deleteById(id))) {
                logger.atInfo().log("account was deleted id={}", id);
                return Map.of("id", id, "message", "Аккаунт пользователя удален");
            } else {
                logger.atWarn().log("account doesn't exist id={}", id);
                return Map.of("id", id, "message", "Аккаунт пользователя не удален, так как не существует");
            }

        }
        logger.atError().log("email={} not found", email);
        return Map.of("error", "Пользователь не найден");
    }

    /**
     * Обработчик ошибки ненайденного пользователя или Jwt
     * @param e исключение, содержащее текст ошибки
     */
    @ExceptionHandler({UserNotFoundException.class, JwtException.class})
    private ResponseEntity<ErrorMessageResponse> handleJwtExceptionException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessageResponse(e.getMessage()));
    }
}
