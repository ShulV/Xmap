package com.shulpov.spots_app.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.shulpov.spots_app.common.dto.FieldErrorDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Общий класс для всех responses в rest api
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    @Schema(description = "Кастомный статус ответа")
    @JsonProperty("custom_status")
    private ApiResponseStatus customStatus;

    @Schema(description = "Сообщение для разработчиков клиентской части (на англ.)", example = "Some error")
    private String message;

    @Schema(description = "Массив ошибок валидации, связанных с определенными полями." +
            "Используется только когда создается или изменяется объект и происходит ошибка валидации")
    @JsonProperty("validation_errors")
    private List<FieldErrorDto> errorDtoList;

    @Schema(description = "Любой DTO объекта. Только для получения одной сущности (dataList - null)")
    private T data;

    @Schema(description = "Массив DTOs объекта. Только для получения списка сущностей (data - null)")
    @JsonProperty("data_list")
    private List<T> dataList;
}

