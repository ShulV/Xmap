package com.shulpov.spots_app.spot_user_infos.utils;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
@Component
public class SpotUserDtoConverter {
    private final ModelMapper modelMapper;

    public SpotUserDtoConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}
