package com.shulpov.spots_app.users.dto;

import com.shulpov.spots_app.image_infos.models.ImageInfo;
import com.shulpov.spots_app.locations.models.City;
import com.shulpov.spots_app.spots.models.Spot;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
@Setter
@Getter
public class FullUserInfoDto {
    @Schema(description = "id пользователя")
    private Long id;

    @Schema(description = "Имя пользователя в приложении")
    private String name;

    @Schema(description = "Электронная почта")
    private String email;

    @Schema(description = "Номер телефона")
    private String phoneNumber;

    @Schema(description = "Дата рождения")
    private Date birthday;

    @Schema(description = "Дата регистрации")
    private Date regDate;

    @Schema(description = "Роль пользователя")
    private String role;

    @Schema(description = "Объекты с информацией об изображениях пользователя")
    private List<ImageInfo> imageInfos;

    @Schema(description = "Созданные споты (те что добавил пользователь)")
    private List<Spot> createdSpots;

    @Schema(description = "Принятые споты (если пользователь имеет роль модератор и выше)")
    private List<Spot> acceptedSpots;

    @Schema(description = "Город")
    private City city;
}
