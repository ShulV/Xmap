package com.shulpov.spots_app.models;

import jakarta.persistence.*;


//TODO VALID ANNOTATION

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "spots")
public class Spot {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "lat")
    private Double latitude;

    @Column(name = "lon")
    private Double longitude;

    @Column(name = "accepted")
    private Boolean accepted;

    @Column(name = "adding_date")
    private LocalDate addingDate;

    @Column(name = "updating_date")
    private LocalDate updatingDate;

    @Column(name = "desc")
    private String description;

    //TODO модер, тип спорта, тип помещения

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "spots_spot_types",
            joinColumns = @JoinColumn(name = "spot_id"),
            inverseJoinColumns = @JoinColumn(name = "spot_type_id")
    )
    private List<SpotType> spotTypes;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "spots_sport_types",
            joinColumns = @JoinColumn(name = "spot_id"),
            inverseJoinColumns = @JoinColumn(name = "sport_type_id")
    )
    private List<SportType> sportTypes;

    @OneToMany(mappedBy = "spot")
    private List<ImageInfo> imageInfos;

    @OneToMany(mappedBy = "spot")
    private List<SpotUser> spotUsers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public List<ImageInfo> getImageInfos() {
        return imageInfos;
    }

    public void setImageInfos(List<ImageInfo> imageInfos) {
        this.imageInfos = imageInfos;
    }

    public List<SpotType> getSpotTypes() {
        return spotTypes;
    }

    public void setSpotTypes(List<SpotType> spotTypes) {
        this.spotTypes = spotTypes;
    }

    public List<SportType> getSportTypes() {
        return sportTypes;
    }

    public void setSportTypes(List<SportType> sportTypes) {
        this.sportTypes = sportTypes;
    }

    public List<SpotUser> getSpotUsers() {
        return spotUsers;
    }

    public void setSpotUsers(List<SpotUser> spotUsers) {
        this.spotUsers = spotUsers;
    }
}
