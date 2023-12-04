package com.shulpov.spots_app.spot_user_infos;

import com.shulpov.spots_app.authentication_management.services.AuthenticationService;
import com.shulpov.spots_app.spots.SpotService;
import com.shulpov.spots_app.spots.models.Spot;
import com.shulpov.spots_app.users.models.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.security.auth.message.AuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
@Tag(name="Контроллер промежуточной таблицы отношения пользователей к местам для катания",
        description="Позволяет изменять состояние лайка и добавления в избранные, " +
                "получать их количество для спота")
@RestController
@RequestMapping("/api/v1/spots-users")
public class SpotUserController {
    private final SpotUserService spotUserService;
    private final SpotService spotService;
    private final AuthenticationService authService;

    @Autowired
    public SpotUserController(SpotUserService spotUserService, @Lazy SpotService spotService,
                              AuthenticationService authService) {
        this.spotUserService = spotUserService;
        this.spotService = spotService;
        this.authService = authService;
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
}
