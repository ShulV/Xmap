package com.shulpov.spots_app.image_infos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Объект для передачи данных о картинке
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
@Getter
@Setter
public class ImageInfoDto implements Serializable {
    @Schema(description = "id информации о картинке", example = "1")
    private Long id;
    @Schema(description = "Путь, по которому можно получить картинку")
    private String url;
    @Schema(description = "Размер картинки", example = "")
    private int size;
    @Schema(description = "Дата выгрузки картинки")
    private Date uploadDate;
}

