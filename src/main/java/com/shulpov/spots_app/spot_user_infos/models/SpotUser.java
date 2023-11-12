package com.shulpov.spots_app.spot_user_infos.models;

import com.shulpov.spots_app.spots.models.Spot;
import com.shulpov.spots_app.users.models.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
@Entity
@Table(name = "spots_users")
@Getter
@Setter
public class SpotUser {
    @EmbeddedId
    private UserSpotPK id;

    @ManyToOne
    @MapsId("spotId")
    @JoinColumn(name = "spot_id")
    private Spot postedSpot;//название не spot, т.к. есть пересечения mappedBy  классе spot

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User userActor;

    @Column(name = "liked")
    private Boolean liked;

    @Column(name = "favorite")
    private Boolean favorite;

    public SpotUser() {
    }

    public SpotUser(Spot postedSpot, User userActor, Boolean liked, Boolean favorite) {
        this.postedSpot = postedSpot;
        this.userActor = userActor;
        this.liked = liked;
        this.favorite = favorite;
    }
}
