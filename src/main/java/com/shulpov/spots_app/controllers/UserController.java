package com.shulpov.spots_app.controllers;

import com.shulpov.spots_app.models.PersonDetails;
import com.shulpov.spots_app.models.User;
import com.shulpov.spots_app.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final static Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //Получить информацию о своем пользователе по токену
    @GetMapping("/get-user-info")
    public Map<String, Object> showUserInfo() {
        logger.atInfo().log("/user-get-info");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        String username = personDetails.getUsername();
        Optional<User> userOpt = userService.findByName(username);

        if(userOpt.isPresent()) {
            logger.atInfo().log("/user-get-info principle exists");
            User user = userOpt.get();
            Map<String, Object> userInfoMap = new HashMap<>();
            userInfoMap.put("name", user.getName());
            userInfoMap.put("email", user.getEmail());
            userInfoMap.put("phone", user.getPhoneNumber());
            userInfoMap.put("birthday", user.getBirthday().toString());//TODO возвращает в формате yyyy-MM-dd
            userInfoMap.put("registrationDate", user.getRegDate().toString());//TODO возвращает в формате yyyy-MM-dd
            logger.atInfo().log("userInfoMap = {}", userInfoMap.toString());
            return userInfoMap;
        }
        //TODO else ??
        logger.atError().log("/user-get-info username={} not found", username);
        return Map.of("error", String.format("Пользователь с именем %s не найден", username));
    }

    //Удалить своего пользователя (по токену)
    @DeleteMapping("/delete-user")
    public Map<String, String> deleteUser() {
        logger.atInfo().log("/delete-user");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        String username = personDetails.getUsername();
        Optional<User> userOpt = userService.findByName(username);
        if(userOpt.isPresent()) {
            logger.atInfo().log("/delete-user principle exists " +
                            "user: id={} name={} email={} phone={} birthday={} regDate={} role={}",
                    userOpt.get().getId(),
                    userOpt.get().getName(),
                    userOpt.get().getEmail(),
                    userOpt.get().getPhoneNumber(),
                    userOpt.get().getBirthday(),
                    userOpt.get().getRegDate(),
                    userOpt.get().getRoleCodeName());
            userService.deleteById(userOpt.get().getId());
            return Map.of("message", "Аккаунт пользователя удален");
        }
        logger.atError().log("/delete-user username={} not found", username);
        return Map.of("error", "Пользователь не найден");
    }
}
