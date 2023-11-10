package com.shulpov.spots_app.users.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shulpov.spots_app.image_infos.dto.ImageInfoDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;
import java.util.List;

@Setter
@Getter
public class MainUserInfoDto {
    @Schema(description = "Имя пользователя", example = "Alex")
    private String name;

    @Schema(description = "Почта пользователя", example = "alex_green@gmail.com")
    private String email;

    @Schema(description = "Номер телефона пользователя", example = "89005553535")
    private String phoneNumber;

    @Schema(description = "Дата рождения пользователя", example = "2000-07-15")
    private Date birthday;

    @Schema(description = "Дата регистрации пользователя", example = "2022-07-15")
    private Date regDate;

    @JsonProperty("imageInfoList")
    private List<ImageInfoDto> imageInfoDtoList;
}
