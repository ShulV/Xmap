package com.shulpov.spots_app.locations.converters;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
@Component
public class LocationDtoConverter {
    private final ModelMapper modelMapper;

    public LocationDtoConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}
