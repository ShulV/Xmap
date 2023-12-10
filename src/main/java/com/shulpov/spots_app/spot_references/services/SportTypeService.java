package com.shulpov.spots_app.spot_references.services;

import com.shulpov.spots_app.spot_references.dto.SportTypeDto;
import com.shulpov.spots_app.spot_references.models.SportType;
import com.shulpov.spots_app.spot_references.repo.SportTypeRepo;
import com.shulpov.spots_app.spot_references.utils.SportTypeDtoConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class SportTypeService {
    private final SportTypeDtoConverter sportTypeDtoConverter;
    private final SportTypeRepo sportTypeRepo;
    private final Logger logger = LoggerFactory.getLogger(SportTypeService.class);

    @Autowired
    public SportTypeService(SportTypeDtoConverter sportTypeDtoConverter, SportTypeRepo sportTypeRepo) {
        this.sportTypeDtoConverter = sportTypeDtoConverter;
        this.sportTypeRepo = sportTypeRepo;
    }

    /**
     * Получить все типы спорта
     */
    public List<SportType> getAll() {
        return sportTypeRepo.findAll();
    }


    /**
     * Получить все типы спорта по списку с их id
     */
    public List<SportType> getByIds(List<Integer> ids) {
        List<SportType> sportTypes =  sportTypeRepo.getByIds(ids);
        if (sportTypes.size() != ids.size()) {
            logger.error("getByIds method. Different size of lists: [ids = '{}', sport_types_size = '{}']", sportTypes.size(),
                    ids.size());
        }
        return sportTypes;
    }

    /**
     * Получить тип спорта по id
     */
    public Optional<SportType> getById(Integer id) {
        return sportTypeRepo.findById(id);
    }

    // DTO ------------------------------------------------------------------------------------

    /**
     * Получить все виды спорта в виде DTO
     */
    public List<SportTypeDto> getAllDto() {
        return getAll().stream().map(sportTypeDtoConverter::convertToDto).toList();
    }

    /**
     * Получить тип спорта по id в виде DTO
     */
    public SportTypeDto getDtoById(Integer id) throws NoSuchElementException {
        Optional<SportType> sportTypeOpt = getById(id);
        if (sportTypeOpt.isEmpty()) {
            logger.error("No sport type: [id = '{}']", id);
            throw new NoSuchElementException();
        }
        return sportTypeDtoConverter.convertToDto(sportTypeOpt.get());

    }

}
