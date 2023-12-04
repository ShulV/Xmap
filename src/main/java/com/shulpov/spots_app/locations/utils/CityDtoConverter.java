package com.shulpov.spots_app.locations.utils;

import com.shulpov.spots_app.common.converters.DtoConvertible;
import com.shulpov.spots_app.locations.dto.CityDto;
import com.shulpov.spots_app.locations.models.City;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class CityDtoConverter implements DtoConvertible<City, CityDto> {
    private final ModelMapper modelMapper;

    @Override
    public CityDto convertToDto(City entity) {
        return modelMapper.map(entity, CityDto.class);
    }
}
