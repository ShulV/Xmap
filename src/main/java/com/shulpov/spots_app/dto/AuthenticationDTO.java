package com.shulpov.spots_app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AuthenticationDTO {
    @NotNull(message = "Email не должен быть пустой")
    @Email(message = "Email должен быть валидным")
    private String email;

    @NotNull(message = "Пароль не должен быть пустой")
    @Size(min = 6, max = 50, message = "Длина пароля должна быть от 6 до 50 символов")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
