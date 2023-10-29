package com.shulpov.spots_app.spot_references.controllers;

import com.shulpov.spots_app.spot_references.dto.SpaceTypeDto;
import com.shulpov.spots_app.spot_references.models.SpaceType;
import com.shulpov.spots_app.spot_references.services.SpaceTypeService;
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
@RequestMapping("/api/space-types")
@Tag(name="Контроллер типов помещений (справочник)", description="Выдает типы помещений")
public class SpaceTypeController {
    private final SpaceTypeService spaceTypeService;

    private final DtoConverter dtoConverter;
    private final Logger logger;

    @Autowired
    public SpaceTypeController(SpaceTypeService spaceTypeService, @Lazy DtoConverter dtoConverter) {
        this.spaceTypeService = spaceTypeService;
        this.dtoConverter = dtoConverter;
        this.logger = LoggerFactory.getLogger(SpaceTypeController.class);
    }

    //Получить все типы помещений
    @Operation(
            summary = "Получение всех типов помещений",
            description = "Позволяет пользователю получить все типы помещений"
    )
    @GetMapping("/get-all")
    public List<SpaceTypeDto> getAllSpaceTypes() {
        logger.atInfo().log("/get-all");
        return spaceTypeService.getAll().stream().map(dtoConverter::spaceTypeToDto).toList();
    }


    //Получить тип помещения по id
    @Operation(
            summary = "Получение конкретного типа помещения",
            description = "Позволяет пользователю получить тип помещения по id"
    )
    @GetMapping("/{id}")
    public SpaceType getSpaceType(@PathVariable(name = "id")  Integer id) throws NoSuchElementException {
        logger.atInfo().log("get-by-id: /{}", id);
        Optional<SpaceType> spaceTypeOpt = spaceTypeService.getById(id);
        if(spaceTypeOpt.isPresent()) {
            return spaceTypeOpt.get();
        } else {
            logger.atError().log("No space type with such id");
            throw new NoSuchElementException("No space type with such id");
        }
    }
}
