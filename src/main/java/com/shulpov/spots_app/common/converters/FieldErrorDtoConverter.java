package com.shulpov.spots_app.common.converters;

import com.shulpov.spots_app.common.dto.FieldErrorDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

/**
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class FieldErrorDtoConverter implements  DtoConvertible<FieldError, FieldErrorDto> {
    private final ModelMapper modelMapper;

    /**
     * Преобразует объект FieldError (ошибка валидации поля) в объект FieldErrorDto.
     *
     * @param entity Объект FieldError, представляющий ошибку валидации поля.
     * @return Объект FieldErrorDto, содержащий информацию об ошибке в удобном для передачи формате.
     * Пример использования:
     * <pre>
     * FieldError fieldError = new FieldError("user", "username", "Имя пользователя уже занято");
     * FieldErrorDto fieldErrorDto = fieldErrorToDto(fieldError);
     * </pre>
     */
    @Override
    public FieldErrorDto convertToDto(FieldError entity) {
        return modelMapper.map(entity, FieldErrorDto.class);
    }
}
