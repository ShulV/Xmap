package com.shulpov.spots_app.spots.dto;


import com.shulpov.spots_app.image_infos.dto.ImageInfoDto;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
@Getter
@Setter
public class SpotDto implements Serializable {
    //TODO VALID ANNOTATION

    private Long id;

    private String name;

    private Double latitude;

    private Double longitude;

    private Boolean accepted;

    private Date addingDate;

    private Date updatingDate;

    private String description;

    private List<ImageInfoDto> imageInfoDtoList;

    private Integer likeNumber;

    private Integer favoriteNumber;

    private List<Integer> spotTypeIds;

    private List<Integer> sportTypeIds;

    private Integer spaceTypeId;
}
