package com.shulpov.spots_app.spots;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shulpov.spots_app.authentication_management.services.AuthenticationService;
import com.shulpov.spots_app.spot_user_infos.SpotUserService;
import com.shulpov.spots_app.spots.dto.SpotDto;
import com.shulpov.spots_app.spots.models.Spot;
import com.shulpov.spots_app.spots.utils.SpotDtoConverter;
import com.shulpov.spots_app.users.models.User;
import com.shulpov.spots_app.users.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.security.auth.message.AuthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
@Tag(name="Контроллер мест для катания", description="Позволяет добавлять и получать споты")
@RestController
@RequestMapping("/api/v1/spots")
public class SpotController {
    private final SpotService spotService;
    private final SpotUserService spotUserService;
    private final UserService userService;
    private final SpotDtoConverter spotDtoConverter;
    private final AuthenticationService authService;
    private final Logger logger = LoggerFactory.getLogger(SpotController.class);

    public SpotController(SpotService spotService, SpotUserService spotUserService, @Lazy UserService userService,
                          SpotDtoConverter spotDtoConverter, AuthenticationService authService) {
        this.spotService = spotService;
        this.spotUserService = spotUserService;
        this.userService = userService;
        this.spotDtoConverter = spotDtoConverter;
        this.authService = authService;
    }

    @Operation(
            summary = "Получение всех спотов",
            description = "Позволяет пользователю получить все споты"
    )
    @GetMapping("/all")
    public List<SpotDto> getAllSpots() {
        return spotService.getAllSpots().stream().map(spotDtoConverter::convertToDto).toList();
    }

    @Operation(
            summary = "Добавление спота",
            description = "Позволяет пользователю добавить спот (отправить на модерацию)",
            security = @SecurityRequirement(name = "accessTokenAuth")
    )
    @PostMapping("/moderation")
    public ResponseEntity<Map<String, Object>> sendToModeration(@RequestParam("files") MultipartFile[] files,
                                           @RequestParam("spotDto") String jsonSpotDto,
                                           Principal principal) throws IOException, AuthException {
        ObjectMapper objectMapper = new ObjectMapper();
        SpotDto spotDto;
        try {
            spotDto = objectMapper.readValue(jsonSpotDto, SpotDto.class);
        } catch (JsonMappingException e) {
            logger.atError().log("Cannot map json to SpotDto.class: jsonSpotDto='{}' e='{}'",
                    jsonSpotDto, e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("errorMessage", "Incorrect spotDto in request param"));
        }

        Spot spot = spotDtoConverter.convertToNewSpot(spotDto);

        String email = principal.getName();
        Optional<User> creatorUserOpt = userService.findByEmail(email);

        if(creatorUserOpt.isPresent()) {
            spot.setCreatorUser(creatorUserOpt.get());
        } else {
            logger.atError().log("No user principle for spot creating");
            throw new AuthException("No user principle for spot creating");
        }
        Spot newSpot = spotService.saveWithAvatars(files, spot);
        return ResponseEntity.ok().body(Map.of("id", newSpot.getId(), "message", "Спот успешно отправлен, модератор должен его подтвердить"));
    }

    @Operation(
            summary = "Получение всех спотов в определенном радиусе",
            description = "Позволяет пользователю получить все споты, находящиеся в определенном радиусе"
    )
    @GetMapping("/in-radius")
    public List<SpotDto> getAllSpots(@RequestParam Double lat,
                                     @RequestParam Double lon,
                                     @RequestParam Double radius) {
        return spotService.getSpotsInRadius(lat, lon, radius).stream().map(spotDtoConverter::convertToDto).toList();
    }

    @Operation(
            summary = "Получение избранных спотов текущего пользователя",
            description = "Позволяет получить избранные споты текущего пользователя",
            security = @SecurityRequirement(name = "accessTokenAuth")
    )
    @GetMapping("/favorite")
    public List<SpotDto> getFavoriteSpots(Principal principal) throws AuthException {
        User user = authService.getUserByPrinciple(principal);
        return spotUserService.getFavoriteSpotUsers(user).stream()
                .map(ss->spotDtoConverter.convertToDto(ss.getPostedSpot())).toList();
    }
}
