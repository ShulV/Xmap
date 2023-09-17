package com.shulpov.spots_app.controllers;

import com.shulpov.spots_app.dto.SpotTypeDto;
import com.shulpov.spots_app.models.SpotType;
import com.shulpov.spots_app.services.SpotTypeService;
import com.shulpov.spots_app.utils.DtoConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/spot-types")
@Tag(name="Контроллер типов мест для катания (справочник)", description="Выдает типы мест для катания")
public class SpotTypeController {
    private final SpotTypeService spotTypeService;
    private final Logger logger = LoggerFactory.getLogger(SpotTypeController.class);

    private final DtoConverter dtoConverter;

    @Autowired
    public SpotTypeController(SpotTypeService spotTypeService, DtoConverter dtoConverter) {
        this.spotTypeService = spotTypeService;
        this.dtoConverter = dtoConverter;
    }

    @Operation(
            summary = "Получение всех типов спотов",
            description = "Позволяет пользователю получить все типы спотов"
    )
    @GetMapping("/get-all")
    public List<SpotTypeDto> getAllSpots() {
        logger.atInfo().log("/get-all");
        return spotTypeService.getAll().stream().map(dtoConverter::spotTypeToDto).toList();
    }

    @Operation(
            summary = "Получение конкретного типа спота",
            description = "Позволяет пользователю получить тип спота по его id"
    )
    @GetMapping("/{id}")
    public SpotType getSpotType(@PathVariable(name = "id")  Integer id) throws NoSuchElementException {
        logger.atInfo().log("get-by-id: /{}", id);
        Optional<SpotType> spotTypeOpt = spotTypeService.getById(id);
        if(spotTypeOpt.isPresent()) {
            return spotTypeOpt.get();
        } else {
            logger.atError().log("No spot type with such id");
            throw new NoSuchElementException("No spot type with such id");
        }
    }
}
