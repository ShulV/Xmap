package com.shulpov.spots_app.locations.services;

import com.shulpov.spots_app.locations.dto.CityDto;
import com.shulpov.spots_app.locations.models.City;
import com.shulpov.spots_app.locations.repo.CityRepo;
import com.shulpov.spots_app.locations.utils.CityDtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Scope(value = "prototype")
@RequiredArgsConstructor
public class CityService {
    private final CityRepo cityRepo;
    private final CityDtoConverter cityDtoConverter;


    /**
     * Получить все города
     */
    public List<City> getAll() {
        return cityRepo.findAll();
    }

    /**
     * Получить город по id
     */
    public Optional<City> getById(Integer id) {
        return cityRepo.findById(id);
    }

    /**
     * Получить список городов по id региона
     * @param id id региона
     */
    public List<City> getByRegionId(Integer id) {
        return cityRepo.findByRegionId(id);
    }

    /**
     * Получить список городов по id страны
     * @param id id страны
     */
    public List<City> getByCountryId(Integer id) {
        return cityRepo.findByCountryId(id);
    }

    // DTO ------------------------------------------------------------------------------------

    /**
     * Получить все города в виде DTO
     */
    public List<CityDto> getAllDto() {
        return getAll().stream().map(cityDtoConverter::convertToDto).toList();
    }

    /**
     * Получить список городов (в виде DTO) по id региона
     * @param id id региона
     */
    public List<CityDto> getDtoByRegionId(Integer id) {
        return getByRegionId(id).stream().map(cityDtoConverter::convertToDto).toList();
    }

    /**
     * Получить список городов (в виде DTO) по id страны
     * @param id id страны
     */
    public List<CityDto> getDtoByCountryId(Integer id) {
        return getByCountryId(id).stream().map(cityDtoConverter::convertToDto).toList();
    }

}