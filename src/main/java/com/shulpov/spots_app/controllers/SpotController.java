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
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/spots/")
public class SpotController {
    private final SpotService spotService;
    private final UserService userService;
    private final SpotValidator spotValidator;
    private final Logger logger = LoggerFactory.getLogger(SpotController.class);

    @Autowired
    public SpotController(SpotService spotService, @Lazy UserService userService, SpotValidator spotValidator) {
        this.spotService = spotService;
        this.userService = userService;
        this.spotValidator = spotValidator;
    }

    //Получить все споты
    @GetMapping("/get-all")
    public List<SpotDto> getAllSpots() {
        logger.atInfo().log("/get-all");
        return spotService.getAllSpots().stream().map(DtoConverter::spotToDto).toList();
    }

    //Добавить спот (отправить на модерацию)
    @PostMapping("/send-to-moderation")
    public Map<String, Object> sendToModeration(@RequestParam("files") MultipartFile[] files,
                                                @RequestParam("spotDto") String jsonSpotDto,
                                                Principal principal) throws IOException, AuthException {
        logger.atInfo().log("/send-to-moderation");
        ObjectMapper objectMapper = new ObjectMapper();
        SpotDto spotDto = objectMapper.readValue(jsonSpotDto, SpotDto.class);
        Spot spot = DtoConverter.dtoToNewSpot(spotDto);

//        spotValidator.validate(spot, bindingResult);//TODO
        Optional<User> creatorUserOpt = userService.findByName(principal.getName());
        if(creatorUserOpt.isPresent()) {
            spot.setCreatorUser(creatorUserOpt.get());
        } else {
            throw new AuthException("No user principle for spot creating");
        }
        spot = spotService.saveWithAvatars(files, spot);
        return Map.of("id", spot.getId());//TODO
    }
}
