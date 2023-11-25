package com.shulpov.spots_app.spot_references.services;

import com.shulpov.spots_app.spot_references.dto.SpotTypeDto;
import com.shulpov.spots_app.spot_references.models.SpotType;
import com.shulpov.spots_app.spot_references.repo.SpotTypeRepo;
import com.shulpov.spots_app.spot_references.utils.SpotTypeDtoConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
public class SpotTypeService {
    private final SpotTypeDtoConverter spotTypeDtoConverter;
    private final SpotTypeRepo spotTypeRepo;
    private final Logger logger = LoggerFactory.getLogger(SpotTypeService.class);

    public SpotTypeService(SpotTypeDtoConverter spotTypeDtoConverter, SpotTypeRepo spotTypeRepo) {
        this.spotTypeDtoConverter = spotTypeDtoConverter;
        this.spotTypeRepo = spotTypeRepo;
    }

    /**
     * Получить все типы спотов
     */
    public List<SpotType> getAll() {
        logger.atInfo().log("getAll");
        return spotTypeRepo.findAll();
    }

    /**
     * Получить все типы спотов в виде DTO
     */
    public List<SpotTypeDto> getAllDto() {
        return getAll().stream().map(spotTypeDtoConverter::convertToDto).toList();
    }

    /**
     * Получить все типы спотов по их id
     */
    public List<SpotType> getByIds(List<Integer> ids) {
        logger.atInfo().log("findByIds ids:{}", ids.toString());
        List<SpotType> spotTypes = new ArrayList<>();
        ids.forEach(id->{
            Optional<SpotType> spotTypeOpt = spotTypeRepo.findById(id);
            if(spotTypeOpt.isPresent()) {
                spotTypes.add(spotTypeOpt.get());
                logger.atInfo().log("id={} exists", id);
            } else {
                logger.atError().log("id={} doesn't exist: ID SPOTTYPE LIST ERROR", id);
            }
        });
        return spotTypes;
    }

    /**
     * Получить тип спота по id
     */
    public Optional<SpotType> getById(Integer id) {
        logger.atInfo().log("getById id={}", id);
        return spotTypeRepo.findById(id);
    }
}
