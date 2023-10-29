package com.shulpov.spots_app.location.services;

import com.shulpov.spots_app.location.models.Country;
import com.shulpov.spots_app.location.repo.CountryRepo;
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
public class CountryService {

    private final CountryRepo countryRepo;

    private final Logger logger = LoggerFactory.getLogger(CountryService.class);

    public CountryService(CountryRepo countryRepo) {
        this.countryRepo = countryRepo;
    }

    public List<Country> getAll() {
        logger.atInfo().log("getAll");
        return countryRepo.findAll();
    }

    //Получить страну по id
    public Optional<Country> getById(Integer id) {
        logger.atInfo().log("getById id={}", id);
        return countryRepo.findById(id);
    }
}