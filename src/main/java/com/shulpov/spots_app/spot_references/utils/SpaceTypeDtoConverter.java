package com.shulpov.spots_app.spot_references.utils;

import com.shulpov.spots_app.common.converters.DtoConvertible;
import com.shulpov.spots_app.spot_references.dto.SpaceTypeDto;
import com.shulpov.spots_app.spot_references.models.SpaceType;
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
public class SpaceTypeDtoConverter implements DtoConvertible<SpaceType, SpaceTypeDto> {
    private final ModelMapper modelMapper;

    @Override
    public SpaceTypeDto convertToDto(SpaceType entity) {
        return modelMapper.map(entity, SpaceTypeDto.class);
    }
}
