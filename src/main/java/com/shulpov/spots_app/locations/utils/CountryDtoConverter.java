package com.shulpov.spots_app.locations.utils;

import com.shulpov.spots_app.common.converters.DtoConvertible;
import com.shulpov.spots_app.locations.dto.CountryDto;
import com.shulpov.spots_app.locations.models.Country;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
@Component
public class CountryDtoConverter implements DtoConvertible<Country, CountryDto> {
    private final ModelMapper modelMapper;

    public CountryDtoConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /**
     * не используется
     */
    @Override
    public Country convertToEntity(CountryDto dto) {
        return null;
    }

    @Override
    public CountryDto convertToDto(Country entity) {
        return modelMapper.map(entity, CountryDto.class);
    }
}
