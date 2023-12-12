package com.shulpov.spots_app.users.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty(value = "phone_number")
    @Schema(description = "Номер телефона")
    private String phoneNumber;

    @Schema(description = "Дата рождения")
    private Date birthday;

    @JsonProperty(value = "registration_date")
    @Schema(description = "Дата регистрации")
    private Date regDate;

    @Schema(description = "Роль пользователя")
    private String role;

    @JsonProperty(value = "image_infos")
    @Schema(description = "Объекты с информацией об изображениях пользователя")
    private List<ImageInfo> imageInfos;

    @JsonProperty("created_spots")
    @Schema(description = "Созданные споты (те что добавил пользователь)")
    private List<Spot> createdSpots;

    @JsonProperty("accepted_spots")
    @Schema(description = "Принятые споты (если пользователь имеет роль модератор и выше)")
    private List<Spot> acceptedSpots;

    @Schema(description = "Город")
    private City city;
}
