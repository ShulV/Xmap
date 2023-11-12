package com.shulpov.spots_app.spot_references.utils;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
@Component
public class SpotReferenceDtoConverter {
    private final ModelMapper modelMapper;


    public SpotReferenceDtoConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}
