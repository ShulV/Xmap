package com.shulpov.spots_app.spot_user_infos.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
@Getter
@Setter
public class SpotUserDto {

    private Long userId;

    private Long spotId;

    private Boolean liked;

    private Boolean favorite;
}
