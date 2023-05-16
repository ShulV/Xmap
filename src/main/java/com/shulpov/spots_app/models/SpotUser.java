package com.shulpov.spots_app.models;

import com.shulpov.spots_app.models.pk.UserSpotPK;
import jakarta.persistence.*;

@Entity
@Table(name = "spots_users")
public class SpotUser {
    @EmbeddedId
    private UserSpotPK id;

    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE })
    @MapsId("spotId")
    @JoinColumn(name = "spot_id")
    private Spot postedSpot;//название не spot, т.к. есть пересечения mappedBy  классе spot

    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE })
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User userActor;

    @Column(name = "liked")
    private Boolean liked;

    @Column(name = "favorite")
    private Boolean favorite;

    public UserSpotPK getId() {
        return id;
    }

    public void setId(UserSpotPK id) {
        this.id = id;
    }

    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public Spot getPostedSpot() {
        return postedSpot;
    }

    public void setPostedSpot(Spot postedSpot) {
        this.postedSpot = postedSpot;
    }

    public User getUserActor() {
        return userActor;
    }

    public void setUserActor(User userActor) {
        this.userActor = userActor;
    }
}
