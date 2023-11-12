package com.shulpov.spots_app.users.dto;

import com.shulpov.spots_app.image_infos.dto.ImageInfoDto;
import com.shulpov.spots_app.spots.dto.SpotDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
@Getter
@Setter
public class UserFullInfoDto implements Serializable {
    @NotEmpty(message = "Имя не должен быть пустым")
    @Size(min = 2, max = 30, message = "Длина имени должна быть от 2 до 30 символов")
    private String name;

    @NotNull(message = "Email не должен быть пустой")
    @Email(message = "Email должен быть валидным")
    @Size(min = 5, max = 50, message = "Длина почты должна быть от 5 до 50 символов")
    private String email;

    @NotEmpty(message = "Номер не должен быть пустым")
    private String phoneNumber;

    @NotNull(message = "Дата дня рождения не должна быть пустой")
    private Date birthday;

    private Date regDate;

    @NotNull(message = "Пароль не должен быть пустой")
    @Size(min = 6, max = 50, message = "Длина пароля должна быть от 6 до 50 символов")
    private String password;

    private List<ImageInfoDto> imageInfoDtoList;

    private List<SpotDto> createdSpots;

    private List<Long> likedSpotIds;

    private List<Long> favoriteSpotIds;
}
