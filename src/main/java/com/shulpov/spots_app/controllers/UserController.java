package com.shulpov.spots_app.controllers;

import com.shulpov.spots_app.models.PersonDetails;
import com.shulpov.spots_app.models.User;
import com.shulpov.spots_app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //Получить информацию о пользователе
    @GetMapping("/get-user-info")
    @ResponseBody
    public Map<String, String> showUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        String username = personDetails.getUsername();
        Optional<User> userOpt = userService.findByName(username);

        if(userOpt.isPresent()) {
            User user = userOpt.get();
            Map<String, String> userInfoMap = new HashMap<>();
            userInfoMap.put("name", user.getName());
            userInfoMap.put("email", user.getEmail());
            userInfoMap.put("phone", user.getPhoneNumber());
            userInfoMap.put("birthday", user.getBirthday().toString());//TODO возвращает в формате yyyy-MM-dd
            userInfoMap.put("registrationDate", user.getRegDate().toString());//TODO возвращает в формате yyyy-MM-dd

            return userInfoMap;
        }
        //TODO else ??

        return Map.of("error", String.format("Пользователь с именем %s не найден", username));
    }

    //Удалить пользователя
    @DeleteMapping("/delete-user")
    @ResponseBody
    public Map<String, String> deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        String username = personDetails.getUsername();
        Optional<User> userOpt = userService.findByName(username);
        if(userOpt.isPresent()) {
            userService.deleteById(userOpt.get().getId());
            return Map.of("message", "Аккаунт пользователя удален");
        }
        return Map.of("error", "Пользователь не найден");
    }
}
