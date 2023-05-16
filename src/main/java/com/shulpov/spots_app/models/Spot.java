package com.shulpov.spots_app.models;

import jakarta.persistence.*;

import java.util.Date;
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
    private Date addingDate;

    @Column(name = "updating_date")
    private Date updatingDate;

    @Column(name = "description")
    private String description;

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST,CascadeType.MERGE })
    @JoinTable(
            name = "spots_spot_types",
            joinColumns = @JoinColumn(name = "spot_id"),
            inverseJoinColumns = @JoinColumn(name = "spot_type_id")
    )
    private List<SpotType> spotTypes;

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST,CascadeType.MERGE })
    @JoinTable(
            name = "spots_sport_types",
            joinColumns = @JoinColumn(name = "spot_id"),
            inverseJoinColumns = @JoinColumn(name = "sport_type_id")
    )
    private List<SportType> sportTypes;

    @OneToMany(mappedBy = "photographedSpot",
            cascade = {CascadeType.PERSIST,CascadeType.MERGE })
    private List<ImageInfo> imageInfos;

    @OneToMany(mappedBy = "postedSpot",
            cascade = {CascadeType.PERSIST,CascadeType.MERGE })
    private List<SpotUser> spotUsers;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "space_type_id", referencedColumnName = "id")
    private SpaceType spaceType;

    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE })
    @JoinColumn(name = "moder_id", referencedColumnName = "id")
    private User moderUser;

    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE })
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User creatorUser;

    @OneToMany(mappedBy = "commentedSpot")
    private List<Comment> comments;

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

    public Date getAddingDate() {
        return addingDate;
    }

    public void setAddingDate(Date addingDate) {
        this.addingDate = addingDate;
    }

    public Date getUpdatingDate() {
        return updatingDate;
    }

    public void setUpdatingDate(Date updatingDate) {
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

    public SpaceType getSpaceType() {
        return spaceType;
    }

    public void setSpaceType(SpaceType spaceType) {
        this.spaceType = spaceType;
    }

    public User getModerUser() {
        return moderUser;
    }

    public void setModerUser(User moderUser) {
        this.moderUser = moderUser;
    }

    public User getCreatorUser() {
        return creatorUser;
    }

    public void setCreatorUser(User creatorUser) {
        this.creatorUser = creatorUser;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
