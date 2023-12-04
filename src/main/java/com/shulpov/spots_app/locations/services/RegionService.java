package com.shulpov.spots_app.locations.services;

import com.shulpov.spots_app.locations.dto.RegionDto;
import com.shulpov.spots_app.locations.models.Region;
import com.shulpov.spots_app.locations.repo.RegionRepo;
import com.shulpov.spots_app.locations.utils.RegionDtoConverter;
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
public class RegionService {
    private final RegionRepo regionRepo;
    private final RegionDtoConverter regionDtoConverter;

    /**
     * Получить список регионов
     */
    public List<Region> getAll() {
        return regionRepo.findAll();
    }

    /**
     * Получить регион по id
     */
    public Optional<Region> getById(Integer id) {
        return regionRepo.findById(id);
    }

    /**
     * Получить список регионов по id города
     */
    public List<Region> getByCountryId(Integer id) {
        return regionRepo.findByCountryId(id);
    }

    // DTO ------------------------------------------------------------------------------------

    /**
     * Получить весь список DTO регионов
     */
    public List<RegionDto> getAllDto() {
        return getAll().stream().map(regionDtoConverter::convertToDto).toList();
    }

    /**
     * Получить список DTO регионов по id города
     */
    public List<RegionDto> getDtoByCountryId(Integer id) {
        return getByCountryId(id).stream().map(regionDtoConverter::convertToDto).toList();
    }

}
