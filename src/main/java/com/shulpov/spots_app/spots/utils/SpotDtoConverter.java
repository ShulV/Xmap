package com.shulpov.spots_app.spots.utils;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
@Component
public class SpotDtoConverter {
    private final ModelMapper modelMapper;

    public SpotDtoConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}
