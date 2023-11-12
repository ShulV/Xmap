package com.shulpov.spots_app.tmp_testing;

import com.shulpov.spots_app.users.models.User;
import com.shulpov.spots_app.users.services.UserService;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

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

    public TestRestController(UserService userService) {
        this.userService = userService;
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
}
