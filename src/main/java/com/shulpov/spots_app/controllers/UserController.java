package com.shulpov.spots_app.controllers;

import com.shulpov.spots_app.dto.UserDto;
import com.shulpov.spots_app.models.PersonDetails;
import com.shulpov.spots_app.models.User;
import com.shulpov.spots_app.services.UserService;
import com.shulpov.spots_app.utils.DtoConverter;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@Tag(name="Контроллер пользователей", description="Позволяет получать информацию о пользователе и удалять аккаунт")
public class UserController {
    private final UserService userService;
    private final DtoConverter dtoConverter;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    public UserController(UserService userService, @Lazy DtoConverter dtoConverter) {
        this.userService = userService;
        this.dtoConverter = dtoConverter;
    }

    //Получить полную информацию о пользователе
    @GetMapping("/get-user")
    public ResponseEntity<UserDto> getAuthUser() {
        logger.atInfo().log("/get-user");
        String username;
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
            username = personDetails.getUsername();
        } catch (UsernameNotFoundException e) {
            logger.atInfo().log("/get-user username={} not found");
            ResponseEntity<UserDto> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return response;
        }
        Optional<User> userOpt = userService.findByName(username);

        if(userOpt.isPresent()) {
            logger.atInfo().log("/user-get-info principle exists");
            User user = userOpt.get();

            return new ResponseEntity(dtoConverter.userToDto(user), HttpStatus.OK);
        }

        logger.atInfo().log("/get-user username={} not found", username);
        ResponseEntity<UserDto> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return response;
    }

    //Удалить своего пользователя (по токену)
    @DeleteMapping("/delete-user")
    public Map<String, Object> deleteUser() {
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
            Long id = userOpt.get().getId();
            if(Boolean.TRUE.equals(userService.deleteById(id))) {
                logger.atInfo().log("account was deleted id={}", id);
                return Map.of("id", id, "message", "Аккаунт пользователя удален");
            } else {
                logger.atInfo().log("account doesn't exist id={}", id);
                return Map.of("id", id, "message", "Аккаунт пользователя не удален, так как не существует");
            }

        }
        logger.atError().log("/delete-user username={} not found", username);
        return Map.of("error", "Пользователь не найден");
    }
}
