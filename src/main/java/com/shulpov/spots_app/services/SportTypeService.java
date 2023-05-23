package com.shulpov.spots_app.services;

import com.shulpov.spots_app.models.SportType;
import com.shulpov.spots_app.repo.SportTypeRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Scope(value = "prototype")
public class SportTypeService {
    private final SportTypeRepo sportTypeRepo;

    private final Logger logger = LoggerFactory.getLogger(SportTypeService.class);

    @Autowired
    public SportTypeService(SportTypeRepo sportTypeRepo) {
        this.sportTypeRepo = sportTypeRepo;
    }

    //Получить все типы спорта
    public List<SportType> getAll() {
        return sportTypeRepo.findAll();
    }

    //Получить все типы спорта по их id
    public List<SportType> getByIds(List<Integer> ids) {
        logger.atInfo().log("findByIds ids:{}", ids.toString());
        List<SportType> sportTypes = new ArrayList<>();
        ids.forEach(id -> {
            Optional<SportType> sportTypeOpt = sportTypeRepo.findById(id);
            if (sportTypeOpt.isPresent()) {
                sportTypes.add(sportTypeOpt.get());
                logger.atInfo().log("id={} exists", id);
            } else {
                logger.atError().log("id={} doesn't exist: ID SPORT TYPE LIST ERROR", id);
            }
        });
        return sportTypes;
    }

    //Получить тип спорта по id
    public Optional<SportType> getById(Integer id) {
        return sportTypeRepo.findById(id);
    }
}
