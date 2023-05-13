package com.shulpov.spots_app.dto;


import java.time.LocalDate;
import java.util.List;

//TODO VALID ANNOTATION

public class SpotDto {
    private String name;

    private Double latitude;

    private Double longitude;

    private Boolean accepted;

    private LocalDate addingDate;

    private LocalDate updatingDate;

    private String description;

    private List<ImageInfoDto> imageInfoDtoList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public LocalDate getAddingDate() {
        return addingDate;
    }

    public void setAddingDate(LocalDate addingDate) {
        this.addingDate = addingDate;
    }

    public LocalDate getUpdatingDate() {
        return updatingDate;
    }

    public void setUpdatingDate(LocalDate updatingDate) {
        this.updatingDate = updatingDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ImageInfoDto> getImageInfoDtoList() {
        return imageInfoDtoList;
    }

    public void setImageInfoDtoList(List<ImageInfoDto> imageInfoDtoList) {
        this.imageInfoDtoList = imageInfoDtoList;
    }
}
