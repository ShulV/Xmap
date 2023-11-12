package com.shulpov.spots_app.spots.models;

import com.shulpov.spots_app.comments.Comment;
import com.shulpov.spots_app.locations.models.City;
import com.shulpov.spots_app.image_infos.models.ImageInfo;
import com.shulpov.spots_app.spot_user_infos.models.SpotUser;
import com.shulpov.spots_app.spot_references.models.SpaceType;
import com.shulpov.spots_app.spot_references.models.SportType;
import com.shulpov.spots_app.spot_references.models.SpotType;
import com.shulpov.spots_app.users.models.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
@Entity
@Table(name = "spots")
@Getter
@Setter
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

    @OneToMany(mappedBy = "postedSpot")
    private List<SpotUser> spotUserList;

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

    @ManyToOne
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    private City city;

    @OneToMany(mappedBy = "commentedSpot")
    private List<Comment> comments;
}
