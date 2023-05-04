package com.shulpov.spots_app.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class UserDTO {
    @NotEmpty(message = "Имя не должен быть пустым")
    @Size(min = 2, max = 30, message = "Длина имени должна быть от 2 до 30 символов")
    private String name;

    @NotNull(message = "Email не должен быть пустой")
    @Email(message = "Email должен быть валидным")
    @Size(min = 5, max = 50, message = "Длина почты должна быть от 5 до 50 символов")
    private String email;

    @NotEmpty(message = "Номер не должен быть пустым")
    @Max(value = 15, message = "В номере телефона должно быть меньше 16 цифр")
    private String phoneNumber;

    @NotNull(message = "Дата дня рождения не должна быть пустой")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthday;

    @NotNull(message = "Пароль не должен быть пустой")
    @Size(min = 6, max = 50, message = "Длина пароля должна быть от 6 до 50 символов")
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
