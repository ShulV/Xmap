package com.shulpov.spots_app.spot_references.services;

import com.shulpov.spots_app.spot_references.dto.SpaceTypeDto;
import com.shulpov.spots_app.spot_references.models.SpaceType;
import com.shulpov.spots_app.spot_references.repo.SpaceTypeRepo;
import com.shulpov.spots_app.spot_references.utils.SpaceTypeDtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
@Service
@Transactional(readOnly = true)
@Scope(value = "prototype")
public class SpaceTypeService {
    private final SpaceTypeDtoConverter spaceTypeDtoConverter;
    private final SpaceTypeRepo spaceTypeRepo;

    @Autowired
    public SpaceTypeService(SpaceTypeDtoConverter spaceTypeDtoConverter, SpaceTypeRepo spaceTypeRepo) {
        this.spaceTypeDtoConverter = spaceTypeDtoConverter;
        this.spaceTypeRepo = spaceTypeRepo;
    }

    /**
     * Получить все типы помещений
     */
    public List<SpaceType> getAll() {
        return spaceTypeRepo.findAll();
    }

    /**
     * Получить все типы помещений в виде DTO
     */
    public List<SpaceTypeDto> getAllDto() {
        return getAll().stream().map(spaceTypeDtoConverter::convertToDto).toList();
    }

    /**
     * Получить тип помещения по id
     */
    public Optional<SpaceType> getById(Integer id) {
        return spaceTypeRepo.findById(id);
    }
}
