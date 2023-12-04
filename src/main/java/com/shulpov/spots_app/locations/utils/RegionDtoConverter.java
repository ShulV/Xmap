package com.shulpov.spots_app.locations.utils;

import com.shulpov.spots_app.common.converters.DtoConvertible;
import com.shulpov.spots_app.locations.dto.RegionDto;
import com.shulpov.spots_app.locations.models.Region;
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
public class RegionDtoConverter implements DtoConvertible<Region, RegionDto> {
    private final ModelMapper modelMapper;

    @Override
    public RegionDto convertToDto(Region entity) {
        return modelMapper.map(entity, RegionDto.class);
    }
}
