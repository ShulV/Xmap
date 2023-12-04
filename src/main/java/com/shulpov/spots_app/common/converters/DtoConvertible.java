package com.shulpov.spots_app.common.converters;

import org.apache.commons.lang3.NotImplementedException;

/**
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
public interface DtoConvertible<E, D> {

    default E convertToEntity(D dto) {
        throw new NotImplementedException("convertToEntity(D dto) not implemented");
    }

    default D convertToDto(E entity) {
        throw new NotImplementedException("convertToDto(E entity) not implemented");
    }
}