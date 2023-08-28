package com.shulpov.spots_app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shulpov.spots_app.dto.SpotDto;
import com.shulpov.spots_app.models.Spot;
import com.shulpov.spots_app.models.User;
import com.shulpov.spots_app.services.SpotService;
import com.shulpov.spots_app.services.UserService;
import com.shulpov.spots_app.utils.DtoConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.security.auth.message.AuthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/spots")
@Tag(name="Контроллер мест для катания", description="Позволяет добавлять и получать споты")
public class SpotController {
    private final SpotService spotService;
    private final UserService userService;
    private final DtoConverter dtoConverter;
    private final Logger logger = LoggerFactory.getLogger(SpotController.class);

    @Autowired
    public SpotController(SpotService spotService, @Lazy UserService userService,
                          @Lazy DtoConverter dtoConverter) {
        this.spotService = spotService;
        this.userService = userService;
        this.dtoConverter = dtoConverter;
    }


    //Получить все споты
    @Operation(
            summary = "Получение всех спотов",
            description = "Позволяет пользователю получить все споты"
    )
    @GetMapping("/get-all")
    public List<SpotDto> getAllSpots() {
        logger.atInfo().log("/get-all");
        return spotService.getAllSpots().stream().map(dtoConverter::spotToDto).toList();
    }

    //Добавить спот (отправить на модерацию)
    @Operation(
            summary = "Добавление спота",
            description = "Позволяет пользователю добавить спот (отправить на модерацию)"
    )
    @PostMapping("/send-to-moderation")
    public Map<String, Object> sendToModeration(@RequestParam("files") MultipartFile[] files,
                                                @RequestParam("spotDto") String jsonSpotDto,
                                                Principal principal) throws IOException, AuthException {
        logger.atInfo().log("/send-to-moderation");
        ObjectMapper objectMapper = new ObjectMapper();
        SpotDto spotDto = objectMapper.readValue(jsonSpotDto, SpotDto.class);
        Spot spot = dtoConverter.dtoToNewSpot(spotDto);

        String name = principal.getName();
        Optional<User> creatorUserOpt = userService.findByName(name);

        if(creatorUserOpt.isPresent()) {
            spot.setCreatorUser(creatorUserOpt.get());
        } else {
            logger.atError().log("No user principle for spot creating");
            throw new AuthException("No user principle for spot creating");
        }
        Spot newSpot = spotService.saveWithAvatars(files, spot);
        return Map.of("id", newSpot.getId());
    }

    //Получить все споты в определенном радиусе
    @Operation(
            summary = "Получение всех спотов в определенном радиусе",
            description = "Позволяет пользователю получить все споты, находящиеся в определенном радиусе"
    )
    @GetMapping("/get-in-radius")
    public List<SpotDto> getAllSpots(@RequestParam Double lat,
                                     @RequestParam Double lon,
                                     @RequestParam Double radius) {
        logger.atInfo().log("/get-in-radius: lat=" + lat + "; lon=" + lon + "; radius=" + radius);
        return spotService.getSpotsInRadius(lat, lon, radius).stream().map(dtoConverter::spotToDto).toList();
    }
}
