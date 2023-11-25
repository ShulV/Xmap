package com.shulpov.spots_app.common.converters;

/**
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
public interface DtoConvertible<E, D> {

    E convertToEntity(D dto);

    D convertToDto(E entity);
}