package com.shulpov.spots_app.tmp_testing;

import com.shulpov.spots_app.authentication_management.services.AuthenticationService;
import com.shulpov.spots_app.spot_user_infos.SpotUserService;
import com.shulpov.spots_app.spot_user_infos.models.SpotUser;
import com.shulpov.spots_app.spots.SpotService;
import com.shulpov.spots_app.spots.models.Spot;
import com.shulpov.spots_app.users.models.User;
import com.shulpov.spots_app.users.services.UserService;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.security.auth.message.AuthException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;


//TODO ТЕСТОВЫЙ КОНТРОЛЛЕР НУЖЕН ТОЛЬКО ДЛЯ УДОБСТВА РУЧНОГО ТЕСТИРОВАНИЯ. ПИСАТЬ Г***О-КОД ТУТ МОЖНО
//TODO СЮДА ВЫКИНУ РОУТЫ, КОТОРЫЕ В КЛИЕНТСКОЙ ЧАСТИ ПОКА ИСПОЛЬЗОВАТЬСЯ НЕ БУДУТ И РЕФАКТОРИТЬ ИХ ПОКА НЕКОГДА И НЕЗАЧЕМ
@RestController
@Tag(description = "Тестовый контроллер для удобства. " +
        "Роуты отсюда не юзать в клиентской части. Нужен для ручного тестирования.",
        name = "Тестовый контроллер")
public class TestRestController {
    private final UserService userService;
    private final SpotService spotService;
    private final AuthenticationService authService;

    private final SpotUserService spotUserService;

    public TestRestController(UserService userService, SpotService spotService, AuthenticationService authService, SpotUserService spotUserService) {
        this.userService = userService;
        this.spotService = spotService;
        this.authService = authService;
        this.spotUserService = spotUserService;
    }

    //TODO этот роут при необходимости можно будет добавить в AuthController , думаю
    @DeleteMapping("/api/v1/user")
    @Schema(description = "удаление своего пользователя, передаем только AccessHeader")
    public Map<String, Object> deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        Optional<User> userOpt = userService.findByEmail(email);
        if(userOpt.isPresent()) {
            Long id = userOpt.get().getId();
            if(Boolean.TRUE.equals(userService.deleteById(id))) {
                return Map.of("id", id, "message", "Аккаунт пользователя удален");
            } else {
                return Map.of("id", id, "message", "Аккаунт пользователя не удален, так как не существует");
            }

        }
        return Map.of("error", "Пользователь не найден");
    }



    @GetMapping("/info/{spotId}")
    public SpotUser getInfo(@PathVariable Long spotId, Principal principal) throws AuthException {
        User user = authService.getUserByPrinciple(principal);
        Spot spot = spotService.findById(spotId).get();
        SpotUser spotUser = spotUserService.getInfo(spot, user);
        return spotUser;
    }

    @GetMapping("/like-number/{spotId}")
    public Map<String, Integer> getLikeNumber(@PathVariable Long spotId) {
        Spot spot = spotService.findById(spotId).get();
        return Map.of("likeNumber", spotUserService.getLikeNumber(spot));
    }

    @GetMapping("/favorite-number/{spotId}")
    public Map<String, Integer> getFavoriteNumber(@PathVariable Long spotId) {
        Spot spot = spotService.findById(spotId).get();
        return Map.of("favoriteNumber", spotUserService.getFavoriteNumber(spot));
    }
}
