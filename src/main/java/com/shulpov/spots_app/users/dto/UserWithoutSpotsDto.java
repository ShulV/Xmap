package com.shulpov.spots_app.users.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shulpov.spots_app.spots.dto.SpotDto;

import java.util.List;


//TODO переделать наследование, чтобы у наследника не урезался функционал, а добавлялся
public class UserWithoutSpotsDto extends UserDto{
    public UserWithoutSpotsDto(UserDto userDto) {
        this.name = userDto.getName();
        this.birthday = userDto.getBirthday();
        this.email = userDto.getEmail();
        this.phoneNumber = userDto.getPhoneNumber();
        this.regDate = userDto.getRegDate();
        this.password = userDto.getPassword();
        this.imageInfoDtoList = userDto.getImageInfoDtoList();
        this.likedSpotIds = userDto.getLikedSpotIds();
        this.favoriteSpotIds = userDto.getFavoriteSpotIds();
    }

    @JsonIgnore
    private List<SpotDto> createdSpots;
}
