package com.shulpov.spots_app.location.services;

import com.shulpov.spots_app.location.models.City;
import com.shulpov.spots_app.location.models.Country;
import com.shulpov.spots_app.location.models.Region;
import com.shulpov.spots_app.location.repo.CityRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Scope(value = "prototype")
public class CityService {
    private final CityRepo cityRepo;

    private final Logger logger = LoggerFactory.getLogger(CityService.class);

    private final RegionService regionService;

    private final CountryService countryService;

    public CityService(CityRepo cityRepo, RegionService regionService, CountryService countryService) {
        this.cityRepo = cityRepo;
        this.regionService = regionService;
        this.countryService = countryService;
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

    public List<City> getByRegionId(Integer id) {
        logger.atInfo().log("getByRegionId id={}", id);
        Optional<Region> optRegion = regionService.getById(id);
        if (optRegion.isPresent()) {
            return cityRepo.findByRegion(optRegion.get());
        } else {
            throw new NotFoundException("Region not found");
        }
    }

    public List<City> getByCountryId(Integer id) {
        logger.atInfo().log("getByCountryId id={}", id);
        Optional<Country> optCountry = countryService.getById(id);
        if (optCountry.isPresent()) {
            return cityRepo.findByRegion_Country(optCountry.get());
        } else {
            throw new NotFoundException("Country not found");
        }
    }

}