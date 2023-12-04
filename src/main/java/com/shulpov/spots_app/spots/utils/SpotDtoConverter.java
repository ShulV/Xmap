package com.shulpov.spots_app.spots.utils;

import com.shulpov.spots_app.common.converters.DtoConvertible;
import com.shulpov.spots_app.image_infos.utils.ImageInfoDtoConverter;
import com.shulpov.spots_app.spot_references.models.SpaceType;
import com.shulpov.spots_app.spot_references.models.SportType;
import com.shulpov.spots_app.spot_references.models.SpotType;
import com.shulpov.spots_app.spot_references.services.SpaceTypeService;
import com.shulpov.spots_app.spot_references.services.SportTypeService;
import com.shulpov.spots_app.spot_references.services.SpotTypeService;
import com.shulpov.spots_app.spot_user_infos.SpotUserService;
import com.shulpov.spots_app.spots.dto.SpotDto;
import com.shulpov.spots_app.spots.models.Spot;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class SpotDtoConverter implements DtoConvertible<Spot, SpotDto> {
    private final  ModelMapper modelMapper;
    private final ImageInfoDtoConverter imageInfoDtoConverter;
    private final SpaceTypeService spaceTypeService;
    private final SpotTypeService spotTypeService;
    private final SportTypeService sportTypeService;
    private final SpotUserService spotUserService;

    /**
     * Для создания спотов. Подставляет текущее время и подтверждение спота в false.
     */
    public Spot convertToNewSpot(SpotDto dto) throws NoSuchElementException {
        Spot spot = modelMapper.map(dto, Spot.class);
        spot.setAccepted(false);
        spot.setAddingDate(new Date(System.currentTimeMillis()));
        spot.setUpdatingDate(new Date(System.currentTimeMillis()));

        Optional<SpaceType> spaceTypeOpt = spaceTypeService.getById(dto.getSpaceTypeId());
        if(spaceTypeOpt.isPresent()) {
            spot.setSpaceType(spaceTypeOpt.get());
        } else {
            throw new NoSuchElementException("Space type with such id not found");
        }

        List<SpotType> spotTypes = spotTypeService.getByIds(dto.getSpotTypeIds());
        spot.setSpotTypes(spotTypes);

        List<SportType> sportTypes = sportTypeService.getByIds(dto.getSportTypeIds());
        spot.setSportTypes(sportTypes);

        spotTypeService.getByIds(dto.getSpotTypeIds());

        return spot;
    }

    @Override
    public SpotDto convertToDto(Spot entity) {
        return SpotDto.builder()
                .id(entity.getId())
                .accepted(entity.getAccepted())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .description(entity.getDescription())
                .name(entity.getName())
                .addingDate(entity.getAddingDate())
                .updatingDate(entity.getUpdatingDate())
                .spotTypeIds(entity.getSpotTypes().stream().map(SpotType::getId).toList())
                .sportTypeIds(entity.getSportTypes().stream().map(SportType::getId).toList())
                .spaceTypeId(entity.getSpaceType().getId())
                //инф о картинках
                .imageInfoDtoList(entity.getImageInfos().stream().map(imageInfoDtoConverter::convertToDto).toList())
                //кол-во избранных
                .favoriteNumber(spotUserService.getFavoriteNumber(entity))
                //кол-во лайков
                .likeNumber(spotUserService.getLikeNumber(entity))
                .build();
    }
}
