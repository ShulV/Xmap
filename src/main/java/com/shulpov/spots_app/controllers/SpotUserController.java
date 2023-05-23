package com.shulpov.spots_app.controllers;

import com.shulpov.spots_app.models.Spot;
import com.shulpov.spots_app.models.User;
import com.shulpov.spots_app.services.SpotService;
import com.shulpov.spots_app.services.SpotUserService;
import com.shulpov.spots_app.services.UserService;
import jakarta.security.auth.message.AuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/spots-users")
public class SpotUserController {
    private final SpotUserService spotUserService;
    private final SpotService spotService;
    private final UserService userService;

    @Autowired
    public SpotUserController(SpotUserService spotUserService, SpotService spotService, UserService userService) {
        this.spotUserService = spotUserService;
        this.spotService = spotService;
        this.userService = userService;
    }

    //Изменить состояние лайка спота для авторизированного пользователя
    @PatchMapping("/change-like-state/{spotId}")
    public Map<String, Object> changeLikeState(@PathVariable Long spotId, Principal principal) throws AuthException {
        //TODO дублируется 8 строк, подумать как вынести общее
        Optional<Spot> spotOpt = spotService.findById(spotId);
        Optional<User> userOpt = userService.findByName(principal.getName());
        if(userOpt.isEmpty()) {
            throw new AuthException("No principal");
        }
        if(spotOpt.isEmpty()) {
            throw new NoSuchElementException("No spot with such id");
        }
        return spotUserService.changeLikeState(spotOpt.get(), userOpt.get());
    }

    //Изменить состояние добавления спота в избранные для авторизированного пользователя
    @PatchMapping("/change-favorite-state/{spotId}")
    public Map<String, Object> changeFavoriteState(@PathVariable Long spotId, Principal principal) throws AuthException {
        //TODO дублируется 8 строк, подумать как вынести общее
        Optional<Spot> spotOpt = spotService.findById(spotId);
        Optional<User> userOpt = userService.findByName(principal.getName());
        if(userOpt.isEmpty()) {
            throw new AuthException("No principal");
        }
        if(spotOpt.isEmpty()) {
            throw new NoSuchElementException("No spot with such id");
        }
        return spotUserService.changeFavoriteState(spotOpt.get(), userOpt.get());
    }

    //Получить количество лайков у спота
    @GetMapping("/get-like-number/{spotId}")
    public Integer getLikeNumber(@PathVariable Long spotId) {
        Optional<Spot> spotOpt = spotService.findById(spotId);
        if(spotOpt.isEmpty()) {
            throw new NoSuchElementException("No spot with id = " + spotId);
        }
        return spotUserService.getLikeNumber(spotOpt.get());
    }

    //Получить количество добавлений в избранное у спота
    @GetMapping("/get-favorite-number/{spotId}")
    public Integer getFavoriteNumber(@PathVariable Long spotId) {
        Optional<Spot> spotOpt = spotService.findById(spotId);
        if(spotOpt.isEmpty()) {
            throw new NoSuchElementException("No spot with id = " + spotId);
        }
        return spotUserService.getFavoriteNumber(spotOpt.get());
    }
}
