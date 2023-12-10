package com.shulpov.spots_app.spot_references.services;

import com.shulpov.spots_app.spot_references.dto.SpotTypeDto;
import com.shulpov.spots_app.spot_references.models.SpotType;
import com.shulpov.spots_app.spot_references.repo.SpotTypeRepo;
import com.shulpov.spots_app.spot_references.utils.SpotTypeDtoConverter;
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
public class SpotTypeService {
    private final SpotTypeDtoConverter spotTypeDtoConverter;
    private final SpotTypeRepo spotTypeRepo;
    private final Logger logger = LoggerFactory.getLogger(SpotTypeService.class);

    /**
     * Получить все типы спотов
     */
    public List<SpotType> getAll() {
        return spotTypeRepo.findAll();
    }

    /**
     * Получить все типы спотов по их id
     */
    public List<SpotType> getByIds(List<Integer> ids) {
        List<SpotType> spotTypes =  spotTypeRepo.getByIds(ids);
        if (spotTypes.size() != ids.size()) {
            logger.error("getByIds method. Different size of lists: [ids = '{}', spot_types_size = '{}']", spotTypes.size(),
                    ids.size());
        }
        return spotTypes;
    }

    /**
     * Получить тип спота по id
     */
    public Optional<SpotType> getById(Integer id) {
        return spotTypeRepo.findById(id);
    }

    // DTO ------------------------------------------------------------------------------------


    /**
     * Получить все типы спотов в виде DTO
     */
    public List<SpotTypeDto> getAllDto() {
        return getAll().stream().map(spotTypeDtoConverter::convertToDto).toList();
    }

    /**
     * Получить тип спота по id в виде DTO
     */
    public SpotTypeDto getDtoById(Integer id) throws NoSuchElementException {
        Optional<SpotType> spotTypeOpt = getById(id);
        if (spotTypeOpt.isEmpty()) {
            logger.error("No spot type: [id = '{}']", id);
            throw new NoSuchElementException();
        }
        return spotTypeDtoConverter.convertToDto(spotTypeOpt.get());

    }
}
