package com.shulpov.spots_app.spot_user_infos;

import com.shulpov.spots_app.authentication_management.services.AuthenticationService;
import com.shulpov.spots_app.spot_user_infos.dto.SpotUserDto;
import com.shulpov.spots_app.spot_user_infos.models.SpotUser;
import com.shulpov.spots_app.spots.SpotService;
import com.shulpov.spots_app.spots.dto.SpotDto;
import com.shulpov.spots_app.spots.models.Spot;
import com.shulpov.spots_app.users.models.User;
import com.shulpov.spots_app.common.utils.DtoConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

/**
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/spots-users")
@Tag(name="Контроллер промежуточной таблицы отношения пользователей к местам для катания",
        description="Позволяет изменять состояние лайка и добавления в избранные, " +
                "получать их количество для спота")

public class SpotUserController {
    private final SpotUserService spotUserService;
    private final SpotService spotService;
    private final AuthenticationService authService;
    private final DtoConverter dtoConverter;

    @Autowired
    public SpotUserController(SpotUserService spotUserService, @Lazy SpotService spotService,
                              AuthenticationService authService, @Lazy DtoConverter dtoConverter) {
        this.spotUserService = spotUserService;
        this.spotService = spotService;
        this.authService = authService;
        this.dtoConverter = dtoConverter;
    }

    /**
     * Позволяет проверить, есть ли определенный спот в базе данных
     */
    private Spot checkSpot(Long spotId) {
        Optional<Spot> spotOpt = spotService.findById(spotId);
        if(spotOpt.isEmpty()) {
            throw new NoSuchElementException("No spot with such id");
        }
        return spotOpt.get();
    }

    @Operation(
            summary = "Изменить состояние лайка",
            description = "Позволяет изменить состояние лайка спота для авторизированного пользователя"
    )
    @PatchMapping("/like-state/{spotId}")
    public Map<String, Object> changeLikeState(@PathVariable Long spotId, Principal principal) throws AuthException {
        User user = authService.getUserByPrinciple(principal);
        Spot spot = checkSpot(spotId);
        return spotUserService.changeLikeState(spot, user);
    }

    @Operation(
            summary = "Изменение состояния добавления спота в избранные",
            description = "Позволяет изменить состояние добавления спота в избранные для авторизированного пользователя"
    )
    @PatchMapping("/favorite-state/{spotId}")
    public Map<String, Object> changeFavoriteState(@PathVariable Long spotId, Principal principal) throws AuthException {
        User user = authService.getUserByPrinciple(principal);
        Spot spot = checkSpot(spotId);
        return spotUserService.changeFavoriteState(spot, user);
    }

    @Operation(
            summary = "Получение количества лайков у спота",
            description = "Позволяет получить количество лайков у спота"
    )
    @GetMapping("/like-number/{spotId}")
    public Map<String, Integer> getLikeNumber(@PathVariable Long spotId) {
        Spot spot = checkSpot(spotId);
        return Map.of("likeNumber", spotUserService.getLikeNumber(spot));
    }

    @Operation(
            summary = "Получение количества добавлений в избранное у спота",
            description = "Позволяет получить количество добавлений в избранное у спота"
    )
    @GetMapping("/favorite-number/{spotId}")
    public Map<String, Integer> getFavoriteNumber(@PathVariable Long spotId) {
        Spot spot = checkSpot(spotId);
        return Map.of("favoriteNumber", spotUserService.getFavoriteNumber(spot));
    }

    @Operation(
            summary = "Получение информации о споте у текущего пользователя",
            description = "Позволяет получить информацию, добавлен ли спот у пользователя в избранные и поставил ли он лайк",
            security = @SecurityRequirement(name = "accessTokenAuth")
    )
    @GetMapping("/info/{spotId}")
    public SpotUserDto getInfo(@PathVariable Long spotId, Principal principal) throws AuthException {
        User user = authService.getUserByPrinciple(principal);
        Spot spot = checkSpot(spotId);
        SpotUser spotUser = spotUserService.getInfo(spot, user);
        return dtoConverter.spotUserToDto(spotUser);
    }

    @Operation(
            summary = "Получение избранных спотов тукущего пользователя",
            description = "Позволяет получить избранные споты текущего пользователя",
            security = @SecurityRequirement(name = "accessTokenAuth")
    )
    @GetMapping("/favorite-spots")
    public List<SpotDto> getFavoriteSpots(Principal principal) throws AuthException {
        User user = authService.getUserByPrinciple(principal);
        return spotUserService.getFavoriteSpotUsers(user).stream()
                .map(ss->dtoConverter.spotToDto(ss.getPostedSpot())).toList();
    }
}
