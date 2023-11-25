package com.shulpov.spots_app.users.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shulpov.spots_app.image_infos.dto.ImageInfoDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
@Setter
@Getter
public class CommentatorDto {
    @Schema(description = "Имя пользователя", example = "Alex")
    private String name;

    @Schema(description = "Почта пользователя", example = "alex_green@gmail.com")
    private String email;

    @JsonProperty("commentator_image_info")
    @Schema(description = "Информация о картинке профиля комментатора")
    private ImageInfoDto commentatorImageInfo;
}
