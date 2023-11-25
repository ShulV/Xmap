package com.shulpov.spots_app.spot_references.controllers;

import com.shulpov.spots_app.spot_references.dto.SportTypeDto;
import com.shulpov.spots_app.spot_references.models.SportType;
import com.shulpov.spots_app.spot_references.services.SportTypeService;
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

/**
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/sport-types")
@Tag(name="Контроллер типов спорта (справочник)", description="Выдает типы спорта")
public class SportTypeController {
    private final SportTypeService sportTypeService;
    private final Logger logger = LoggerFactory.getLogger(SportTypeController.class);

    @Autowired
    public SportTypeController(SportTypeService sportTypeService) {
        this.sportTypeService = sportTypeService;
    }

    @Operation(
            summary = "Получение всех типов спорта",
            description = "Позволяет пользователю получить все типы спотов"
    )
    @GetMapping("/all")
    public List<SportTypeDto> getAllSportTypes() {
        return sportTypeService.getAllDto();
    }

    @Operation(
            summary = "Получение конкретного типа спорта",
            description = "Позволяет пользователю получить тип спорта по id"
    )
    @GetMapping("/{id}")
    public SportType getSportType(@PathVariable(name = "id")  Integer id) throws NoSuchElementException {
        Optional<SportType> sportTypeOpt = sportTypeService.getById(id);
        if(sportTypeOpt.isPresent()) {
            return sportTypeOpt.get();
        } else {
            logger.atError().log("No sport type with such id");
            throw new NoSuchElementException("No sport type with such id");
        }
    }
}
