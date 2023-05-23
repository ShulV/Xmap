package com.shulpov.spots_app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shulpov.spots_app.dto.SpotDto;
import com.shulpov.spots_app.models.Spot;
import com.shulpov.spots_app.models.User;
import com.shulpov.spots_app.services.SpotService;
import com.shulpov.spots_app.services.UserService;
import com.shulpov.spots_app.utils.DtoConverter;
import com.shulpov.spots_app.utils.validators.SpotValidator;
import jakarta.security.auth.message.AuthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/spots")
public class SpotController {
    private final SpotService spotService;
    private final UserService userService;
    private final SpotValidator spotValidator;

    private final DtoConverter dtoConverter;
    private final Logger logger = LoggerFactory.getLogger(SpotController.class);

    @Autowired
    public SpotController(SpotService spotService, UserService userService, SpotValidator spotValidator, DtoConverter dtoConverter) {
        this.spotService = spotService;
        this.userService = userService;
        this.spotValidator = spotValidator;
        this.dtoConverter = dtoConverter;
    }

    //Получить все споты
    @GetMapping("/get-all")
    public List<SpotDto> getAllSpots() {
        logger.atInfo().log("/get-all");
        return spotService.getAllSpots().stream().map(dtoConverter::spotToDto).toList();
    }

    //Добавить спот (отправить на модерацию)
    @PostMapping("/send-to-moderation")
    public Map<String, Object> sendToModeration(@RequestParam("files") MultipartFile[] files,
                                                @RequestParam("spotDto") String jsonSpotDto,
                                                Principal principal) throws IOException, AuthException {
        logger.atInfo().log("/send-to-moderation");
        ObjectMapper objectMapper = new ObjectMapper();
        SpotDto spotDto = objectMapper.readValue(jsonSpotDto, SpotDto.class);
        Spot spot = dtoConverter.dtoToNewSpot(spotDto);

//        spotValidator.validate(spot, bindingResult);//TODO
        String name = principal.getName();
        Optional<User> creatorUserOpt = userService.findByName(name);

        if(creatorUserOpt.isPresent()) {
            spot.setCreatorUser(creatorUserOpt.get());
        } else {
            throw new AuthException("No user principle for spot creating");
        }
        Spot newSpot = spotService.saveWithAvatars(files, spot);
        return Map.of("id", newSpot.getId());//TODO
    }
}
