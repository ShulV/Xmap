package com.shulpov.spots_app.services;

import com.shulpov.spots_app.models.City;
import com.shulpov.spots_app.repo.CityRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Scope(value = "prototype")
public class CityService {
    private final CityRepo cityRepo;

    private final Logger logger = LoggerFactory.getLogger(CityService.class);

    public CityService(CityRepo cityRepo) {
        this.cityRepo = cityRepo;
    }

    public List<City> getAll() {
        logger.atInfo().log("getAll");
        return cityRepo.findAll();
    }

    //Получить город по id
    public Optional<City> getById(Integer id) {
        logger.atInfo().log("getById id={}", id);
        return cityRepo.findById(id);
    }
}
