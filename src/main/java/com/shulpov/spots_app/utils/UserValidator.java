package com.shulpov.spots_app.utils;

import com.shulpov.spots_app.models.User;
import com.shulpov.spots_app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {
    private final UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        boolean isExistingName = userService.findByName(user.getName()).isPresent();
        if(isExistingName) {
            // поле, код ошибки, сообщение ошибки
            errors.rejectValue("name", "", "Пользователь с таким именем уже существует");
        }

        boolean isExistingEmail = userService.findByEmail(user.getEmail()).isPresent();
        if(isExistingEmail) {
            // поле, код ошибки, сообщение ошибки
            errors.rejectValue("email", "", "Пользователь с таким почтовым ящиком уже существует");
        }

        boolean isExistingPhone = userService.findByPhoneNumber(user.getPhoneNumber()).isPresent();
        if(isExistingPhone) {
            // поле, код ошибки, сообщение ошибки
            errors.rejectValue("phoneNumber", "", "Пользователь с таким телефоном уже существует");
        }

    }
}
