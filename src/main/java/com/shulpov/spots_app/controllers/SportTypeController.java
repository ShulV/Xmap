package com.shulpov.spots_app.controllers;

import com.shulpov.spots_app.dto.SportTypeDto;
import com.shulpov.spots_app.models.SportType;
import com.shulpov.spots_app.services.SportTypeService;
import com.shulpov.spots_app.utils.DtoConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/sport-types")
@Tag(name="Контроллер типов спорта (справочник)", description="Выдает типы спорта")
public class SportTypeController {
    private final SportTypeService sportTypeService;

    private final DtoConverter dtoConverter;
    private final Logger logger = LoggerFactory.getLogger(SportTypeController.class);

    @Autowired
    public SportTypeController(SportTypeService sportTypeService, @Lazy DtoConverter dtoConverter) {
        this.sportTypeService = sportTypeService;
        this.dtoConverter = dtoConverter;
    }

    //Получить все типы спорта
    @Operation(
            summary = "Получение всех типов спорта",
            description = "Позволяет пользователю получить все типы спотов"
    )
    @GetMapping("/get-all")
    public List<SportTypeDto> getAllSportTypes() {
        logger.atInfo().log("/get-all");
        return sportTypeService.getAll().stream().map(dtoConverter::sportTypeToDto).toList();
    }

    //Получить тип спорта по id
    @Operation(
            summary = "Получение конкретного типа спорта",
            description = "Позволяет пользователю получить тип спорта по id"
    )
    @GetMapping("/{id}")
    public SportType getSportType(@PathVariable(name = "id")  Integer id) throws NoSuchElementException {
        logger.atInfo().log("get-by-id: /{}", id);
        Optional<SportType> sportTypeOpt = sportTypeService.getById(id);
        if(sportTypeOpt.isPresent()) {
            return sportTypeOpt.get();
        } else {
            logger.atError().log("No sport type with such id");
            throw new NoSuchElementException("No sport type with such id");
        }
    }
}
