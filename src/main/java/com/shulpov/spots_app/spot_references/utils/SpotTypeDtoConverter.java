package com.shulpov.spots_app.spot_references.utils;

import com.shulpov.spots_app.common.converters.DtoConvertible;
import com.shulpov.spots_app.spot_references.dto.SpotTypeDto;
import com.shulpov.spots_app.spot_references.models.SpotType;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
@Component
public class SpotTypeDtoConverter implements DtoConvertible<SpotType, SpotTypeDto> {
    private final ModelMapper modelMapper;

    public SpotTypeDtoConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /**
     * не используется
     */
    @Override
    public SpotType convertToEntity(SpotTypeDto dto) {
        return null;
    }

    @Override
    public SpotTypeDto convertToDto(SpotType entity) {
        return modelMapper.map(entity, SpotTypeDto.class);
    }
}
