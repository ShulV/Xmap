package com.shulpov.spots_app.locations.utils;

import com.shulpov.spots_app.common.converters.DtoConvertible;
import com.shulpov.spots_app.locations.dto.CountryDto;
import com.shulpov.spots_app.locations.models.Country;
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
public class CountryDtoConverter implements DtoConvertible<Country, CountryDto> {
    private final ModelMapper modelMapper;

    @Override
    public CountryDto convertToDto(Country entity) {
        return modelMapper.map(entity, CountryDto.class);
    }
}
