package com.shulpov.spots_app.spot_references.services;

import com.shulpov.spots_app.spot_references.dto.SpaceTypeDto;
import com.shulpov.spots_app.spot_references.models.SpaceType;
import com.shulpov.spots_app.spot_references.repo.SpaceTypeRepo;
import com.shulpov.spots_app.spot_references.utils.SpaceTypeDtoConverter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
@Service
@Transactional(readOnly = true)
@Scope(value = "prototype")
@RequiredArgsConstructor
public class SpaceTypeService {
    private final SpaceTypeDtoConverter spaceTypeDtoConverter;
    private final SpaceTypeRepo spaceTypeRepo;
    private final Logger logger = LoggerFactory.getLogger(SpaceTypeService.class);

    /**
     * Получить все типы помещений
     */
    public List<SpaceType> getAll() {
        return spaceTypeRepo.findAll();
    }

    /**
     * Получить тип помещения по id
     */
    public Optional<SpaceType> getById(Integer id) {
        return spaceTypeRepo.findById(id);
    }

    // DTO ------------------------------------------------------------------------------------

    /**
     * Получить все типы помещений в виде DTO
     */
    public List<SpaceTypeDto> getAllDto() {
        return getAll().stream().map(spaceTypeDtoConverter::convertToDto).toList();
    }

    /**
     * Получить тип помещения в виде DTO по его id
     */
    public SpaceTypeDto getDtoById(Integer id) throws NoSuchElementException {
        Optional<SpaceType> spaceTypeOpt = getById(id);
        if (spaceTypeOpt.isEmpty()) {
            logger.error("Space type is not existing: [spot_type_id = '{}']", id);
            throw new NoSuchElementException();
        } else {
            return spaceTypeDtoConverter.convertToDto(spaceTypeOpt.get());
        }
    }
}
