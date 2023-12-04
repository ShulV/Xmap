package com.shulpov.spots_app.spot_references.utils;

import com.shulpov.spots_app.common.converters.DtoConvertible;
import com.shulpov.spots_app.spot_references.dto.SportTypeDto;
import com.shulpov.spots_app.spot_references.models.SportType;
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
public class SportTypeDtoConverter implements DtoConvertible<SportType, SportTypeDto> {
    private final ModelMapper modelMapper;

    @Override
    public SportTypeDto convertToDto(SportType entity) {
        return modelMapper.map(entity, SportTypeDto.class);
    }
}

