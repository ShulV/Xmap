package com.shulpov.spots_app.controllers;

import com.shulpov.spots_app.dto.SpotDto;
import com.shulpov.spots_app.dto.SpotUserDto;
import com.shulpov.spots_app.models.Spot;
import com.shulpov.spots_app.models.SpotUser;
import com.shulpov.spots_app.user.User;
import com.shulpov.spots_app.services.SpotService;
import com.shulpov.spots_app.services.SpotUserService;
import com.shulpov.spots_app.services.UserService;
import com.shulpov.spots_app.utils.DtoConverter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.security.auth.message.AuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/spots-users")
@Tag(name="Контроллер промежуточной таблицы отношения пользователей к местам для катания",
        description="Позволяет изменять состояние лайка и добавления в избранные, " +
                "получать количество их количество для спота")

public class SpotUserController {
    private final SpotUserService spotUserService;
    private final SpotService spotService;
    private final UserService userService;

    private final DtoConverter dtoConverter;

    @Autowired
    public SpotUserController(SpotUserService spotUserService, @Lazy SpotService spotService,
                              @Lazy UserService userService, @Lazy DtoConverter dtoConverter) {
        this.spotUserService = spotUserService;
        this.spotService = spotService;
        this.userService = userService;
        this.dtoConverter = dtoConverter;
    }

    //Проверить спот на существование
    private Spot checkSpot(Long spotId) {
        Optional<Spot> spotOpt = spotService.findById(spotId);
        if(spotOpt.isEmpty()) {
            throw new NoSuchElementException("No spot with such id");
        }
        return spotOpt.get();
    }

    //Проверить юзера на существование
    private User checkUser(Principal principal) throws AuthException {
        Optional<User> userOpt = userService.findByEmail(principal.getName());
        if(userOpt.isEmpty()) {
            throw new AuthException("No principal");
        }
        return userOpt.get();
    }

    //Изменить состояние лайка спота для авторизированного пользователя
    @PatchMapping("/change-like-state/{spotId}")
    public Map<String, Object> changeLikeState(@PathVariable Long spotId, Principal principal) throws AuthException {
        User user = checkUser(principal);
        Spot spot = checkSpot(spotId);
        return spotUserService.changeLikeState(spot, user);
    }

    //Изменить состояние добавления спота в избранные для авторизированного пользователя
    @PatchMapping("/change-favorite-state/{spotId}")
    public Map<String, Object> changeFavoriteState(@PathVariable Long spotId, Principal principal) throws AuthException {
        User user = checkUser(principal);
        Spot spot = checkSpot(spotId);
        return spotUserService.changeFavoriteState(spot, user);
    }

    //Получить количество лайков у спота
    @GetMapping("/get-like-number/{spotId}")
    public Integer getLikeNumber(@PathVariable Long spotId) {
        Spot spot = checkSpot(spotId);
        return spotUserService.getLikeNumber(spot);
    }

    //Получить количество добавлений в избранное у спота
    @GetMapping("/get-favorite-number/{spotId}")
    public Integer getFavoriteNumber(@PathVariable Long spotId) {
        Spot spot = checkSpot(spotId);
        return spotUserService.getFavoriteNumber(spot);
    }

    //Получить информацию, добавлен ли спот у текущего пользователя в избранные и поставил ли он лайк
    @GetMapping("/get-info/{spotId}")
    public SpotUserDto getInfo(@PathVariable Long spotId, Principal principal) throws AuthException {
        User user = checkUser(principal);
        Spot spot = checkSpot(spotId);
        SpotUser spotUser = spotUserService.getInfo(spot, user);
        return dtoConverter.spotUserToDto(spotUser);
    }

    //Получить избранные споты текущего пользователя
    @GetMapping("/get-favorite-spots")
    public List<SpotDto> getFavoriteSpots(Principal principal) throws AuthException {
        User user = checkUser(principal);
        return spotUserService.getFavoriteSpotUsers(user).stream()
                .map(ss->dtoConverter.spotToDto(ss.getPostedSpot())).toList();
    }
}
