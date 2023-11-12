package com.shulpov.spots_app.authentication_management.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @Schema(description = "Имя пользователя", example = "Alex")
    @NotEmpty(message = "Имя не должен быть пустым")
    @Size(min = 2, max = 30, message = "Длина имени должна быть от 2 до 30 символов")
    private String name;

    @Schema(description = "Почта пользователя", example = "alex_green@gmail.com")
    @NotEmpty(message = "Email не должен быть пустой")
    @Email(message = "Email должен быть валидным")
    @Size(min = 5, max = 50, message = "Длина почты должна быть от 5 до 50 символов")
    private String email;

    @Schema(description = "Номер телефона пользователя", example = "89005553535")
    @NotEmpty(message = "Номер не должен быть пустым")
    @Pattern(regexp = "^\\+?[0-9\\-\\s()]*$", message = "Неверный формат номера телефона")
    private String phoneNumber;

    @Schema(description = "Дата рождения пользователя", example = "2000-07-15")
    @NotNull(message = "Дата дня рождения не должна быть пустой")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    @Schema(description = "Пароль пользователя", example = "password")
    @NotEmpty(message = "Пароль не должен быть пустой")
    @Size(min = 6, max = 50, message = "Длина пароля должна быть от 6 до 50 символов")
    private String password;
}