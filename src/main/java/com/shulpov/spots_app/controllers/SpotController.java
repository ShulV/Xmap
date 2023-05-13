package com.shulpov.spots_app.controllers;

import com.shulpov.spots_app.dto.SpotDto;
import com.shulpov.spots_app.dto.UserDto;
import com.shulpov.spots_app.models.Spot;
import com.shulpov.spots_app.models.User;
import com.shulpov.spots_app.services.SpotService;
import com.shulpov.spots_app.utils.DtoConverter;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/spots/")
public class SpotController {
    private final SpotService spotService;
    private final static Logger logger = LoggerFactory.getLogger(SpotController.class);

    @Autowired
    public SpotController(SpotService spotService) {
        this.spotService = spotService;
    }

    //Получить все споты
    @GetMapping("/get-all")
    public List<SpotDto> getAllSpots() {
        logger.atInfo().log("/get-all");
        return spotService.getAllSpots().stream().map(DtoConverter::spotToDto).toList();
    }

    //Добавить спот (отправить на модерацию)
    @PostMapping("/send-to-moderation")
    public Map<String, String> sendToModeration(@RequestBody @Valid SpotDto spotDto,
                                                BindingResult bindingResult) {
        logger.atInfo().log("/send-to-moderation");

        Spot spot = DtoConverter.dtoToNewSpot(spotDto);
        //spotValidator.validate(spot, bindingResult);//TODO
        return Map.of("", "");//TODO
    }
}
