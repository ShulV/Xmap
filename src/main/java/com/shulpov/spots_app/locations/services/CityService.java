package com.shulpov.spots_app.locations.services;

import com.shulpov.spots_app.locations.dto.CityDto;
import com.shulpov.spots_app.locations.models.City;
import com.shulpov.spots_app.locations.models.Country;
import com.shulpov.spots_app.locations.models.Region;
import com.shulpov.spots_app.locations.repo.CityRepo;
import com.shulpov.spots_app.locations.utils.CityDtoConverter;
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

    private final CityDtoConverter cityDtoConverter;

    private final RegionService regionService;

    private final CountryService countryService;

    public CityService(CityRepo cityRepo, CityDtoConverter cityDtoConverter, RegionService regionService, CountryService countryService) {
        this.cityRepo = cityRepo;
        this.cityDtoConverter = cityDtoConverter;
        this.regionService = regionService;
        this.countryService = countryService;
    }

    /**
     * Получить все города
     */
    public List<City> getAll() {
        logger.atInfo().log("getAll");
        return cityRepo.findAll();
    }

    /**
     * Получить все города в виде DTO
     */
    public List<CityDto> getAllDto() {
        return getAll().stream().map(cityDtoConverter::convertToDto).toList();
    }

    /**
     * Получить город по id
     */
    public Optional<City> getById(Integer id) {
        logger.atInfo().log("getById id={}", id);
        return cityRepo.findById(id);
    }

    /**
     * Получить список городов по id региона
     * @param id id региона
     */
    public List<City> getByRegionId(Integer id) {
        Optional<Region> optRegion = regionService.getById(id);
        if (optRegion.isPresent()) {
            return cityRepo.findByRegion(optRegion.get());
        } else {
            throw new NotFoundException("Region not found");
        }
    }

    /**
     * Получить список городов (в виде DTO) по id региона
     * @param id id региона
     */
    public List<CityDto> getDtoByRegionId(Integer id) {
        return getByRegionId(id).stream().map(cityDtoConverter::convertToDto).toList();
    }

    /**
     * Получить список городов по id страны
     * @param id id страны
     */
    public List<City> getByCountryId(Integer id) {
        logger.atInfo().log("getByCountryId id={}", id);
        Optional<Country> optCountry = countryService.getById(id);
        if (optCountry.isPresent()) {
            //hibernat'у нужно нижнее подчеркивание для того, чтобы не путать таблицу с полем (само генерится)
            return cityRepo.findByRegion_Country(optCountry.get());
        } else {
            throw new NotFoundException("Country not found");
        }
    }

    /**
     * Получить список городов (в виде DTO) по id страны
     * @param id id страны
     */
    public List<CityDto> getDtoByCountryId(Integer id) {
        return getByCountryId(id).stream().map(cityDtoConverter::convertToDto).toList();
    }

}