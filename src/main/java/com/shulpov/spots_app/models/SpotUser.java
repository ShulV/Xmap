package com.shulpov.spots_app.models;

import com.shulpov.spots_app.models.pk.UserSpotPK;
import jakarta.persistence.*;

@Entity
@Table(name = "spots_users")
public class SpotUser {
    @EmbeddedId
    private UserSpotPK id;

    @ManyToOne
    @MapsId("spotId")
    @JoinColumn(name = "spot_id")
    private Spot spot;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

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

    public Spot getSpot() {
        return spot;
    }

    public void setSpot(Spot spot) {
        this.spot = spot;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
