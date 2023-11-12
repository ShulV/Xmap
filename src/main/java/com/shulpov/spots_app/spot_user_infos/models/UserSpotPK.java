package com.shulpov.spots_app.spot_user_infos.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Составной первичный ключ для промежуточной таблицы spots_users
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class UserSpotPK implements Serializable {
    @Column(name = "spot_id")
    private Long spotId;

    @Column(name = "user_id")
    private Long userId;
}
