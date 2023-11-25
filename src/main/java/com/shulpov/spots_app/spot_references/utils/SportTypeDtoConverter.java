package com.shulpov.spots_app.spot_references.utils;

import com.shulpov.spots_app.common.converters.DtoConvertible;
import com.shulpov.spots_app.spot_references.dto.SportTypeDto;
import com.shulpov.spots_app.spot_references.models.SportType;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
@Component
public class SportTypeDtoConverter implements DtoConvertible<SportType, SportTypeDto> {
    private final ModelMapper modelMapper;

    public SportTypeDtoConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /**
     * не используется
     */
    @Override
    public SportType convertToEntity(SportTypeDto dto) {
        return null;
    }

    @Override
    public SportTypeDto convertToDto(SportType entity) {
        return modelMapper.map(entity, SportTypeDto.class);
    }
}

