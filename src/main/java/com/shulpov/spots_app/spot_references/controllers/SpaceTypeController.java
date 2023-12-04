package com.shulpov.spots_app.spot_references.controllers;

import com.shulpov.spots_app.spot_references.dto.SpaceTypeDto;
import com.shulpov.spots_app.spot_references.models.SpaceType;
import com.shulpov.spots_app.spot_references.services.SpaceTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Tag(name="Контроллер типов помещений (справочник)", description="Выдает типы помещений")
@RestController
@RequestMapping("/api/v1/space-types")
@RequiredArgsConstructor()
public class SpaceTypeController {
    private final Logger logger = LoggerFactory.getLogger(SpaceTypeController.class);
    private final SpaceTypeService spaceTypeService;

    @Operation(
            summary = "Получение всех типов помещений",
            description = "Позволяет пользователю получить все типы помещений"
    )
    @GetMapping("/all")
    public List<SpaceTypeDto> getAllSpaceTypes() {
        return spaceTypeService.getAllDto();
    }

    @Operation(
            summary = "Получение конкретного типа помещения",
            description = "Позволяет пользователю получить тип помещения по id"
    )
    @GetMapping("/{id}")
    public SpaceType getSpaceType(@PathVariable(name = "id")  Integer id) throws NoSuchElementException {
        Optional<SpaceType> spaceTypeOpt = spaceTypeService.getById(id);
        if(spaceTypeOpt.isPresent()) {
            return spaceTypeOpt.get();
        } else {
            logger.atError().log("No space type with such id");
            throw new NoSuchElementException("No space type with such id");
        }
    }
}
