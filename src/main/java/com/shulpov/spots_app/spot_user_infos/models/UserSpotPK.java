package com.shulpov.spots_app.spot_user_infos.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

//Составной первичный ключ для промежуточной таблицы spots_users
@Embeddable
public class UserSpotPK implements Serializable {
    @Column(name = "spot_id")
    private Long spotId;

    @Column(name = "user_id")
    private Long userId;

    public UserSpotPK(Long spotId, Long userId) {
        this.spotId = spotId;
        this.userId = userId;
    }

    public UserSpotPK() {
    }

    public Long getSpotId() {
        return spotId;
    }

    public void setSpotId(Long spotId) {
        this.spotId = spotId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
