package com.shulpov.spots_app.locations.services;

import com.shulpov.spots_app.locations.dto.CountryDto;
import com.shulpov.spots_app.locations.models.Country;
import com.shulpov.spots_app.locations.repo.CountryRepo;
import com.shulpov.spots_app.locations.utils.CountryDtoConverter;
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
public class CountryService {

    private final CountryRepo countryRepo;
    private final CountryDtoConverter countryDtoConverter;

    /**
     * Получить все страны
     */
    public List<Country> getAll() {
        return countryRepo.findAll();
    }

    /**
     * Получить все DTO стран
     */
    public List<CountryDto> getAllDto() {
        return getAll().stream().map(countryDtoConverter::convertToDto).toList();
    }


    //Получить страну по id
    public Optional<Country> getById(Integer id) {
        return countryRepo.findById(id);
    }
}