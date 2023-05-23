package com.shulpov.spots_app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class UserDto implements Serializable {
    @NotEmpty(message = "Имя не должен быть пустым")
    @Size(min = 2, max = 30, message = "Длина имени должна быть от 2 до 30 символов")
    protected String name;

    @NotNull(message = "Email не должен быть пустой")
    @Email(message = "Email должен быть валидным")
    @Size(min = 5, max = 50, message = "Длина почты должна быть от 5 до 50 символов")
    protected String email;

    @NotEmpty(message = "Номер не должен быть пустым")
    protected String phoneNumber;

    @NotNull(message = "Дата дня рождения не должна быть пустой")
    protected Date birthday;

    protected Date regDate;

    @NotNull(message = "Пароль не должен быть пустой")
    @Size(min = 6, max = 50, message = "Длина пароля должна быть от 6 до 50 символов")
    protected String password;

    protected List<ImageInfoDto> imageInfoDtoList;

    protected List<SpotDto> createdSpots;

    protected List<Long> likedSpotIds;

    protected List<Long> favoriteSpotIds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<ImageInfoDto> getImageInfoDtoList() {
        return imageInfoDtoList;
    }
    public void setImageInfoDtoList(List<ImageInfoDto> imageInfoDtoList) {
        this.imageInfoDtoList = imageInfoDtoList;
    }

    public List<SpotDto> getCreatedSpots() {
        return createdSpots;
    }

    public void setCreatedSpots(List<SpotDto> createdSpots) {
        this.createdSpots = createdSpots;
    }

    public List<Long> getLikedSpotIds() {
        return likedSpotIds;
    }

    public void setLikedSpotIds(List<Long> likedSpotIds) {
        this.likedSpotIds = likedSpotIds;
    }

    public List<Long> getFavoriteSpotIds() {
        return favoriteSpotIds;
    }

    public void setFavoriteSpotIds(List<Long> favoriteSpotIds) {
        this.favoriteSpotIds = favoriteSpotIds;
    }
}
