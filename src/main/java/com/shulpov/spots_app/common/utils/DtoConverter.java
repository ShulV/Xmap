package com.shulpov.spots_app.common.utils;

import com.shulpov.spots_app.common.dto.FieldErrorDto;
import com.shulpov.spots_app.image_infos.dto.ImageInfoDto;
import com.shulpov.spots_app.image_infos.utils.ImageInfoDtoConverter;
import com.shulpov.spots_app.locations.dto.CityDto;
import com.shulpov.spots_app.locations.dto.CountryDto;
import com.shulpov.spots_app.locations.dto.RegionDto;
import com.shulpov.spots_app.locations.models.City;
import com.shulpov.spots_app.locations.models.Country;
import com.shulpov.spots_app.locations.models.Region;
import com.shulpov.spots_app.spot_references.dto.SpaceTypeDto;
import com.shulpov.spots_app.spot_references.dto.SportTypeDto;
import com.shulpov.spots_app.spot_references.dto.SpotTypeDto;
import com.shulpov.spots_app.spot_references.models.SpaceType;
import com.shulpov.spots_app.spot_references.models.SportType;
import com.shulpov.spots_app.spot_references.models.SpotType;
import com.shulpov.spots_app.spot_references.services.SpaceTypeService;
import com.shulpov.spots_app.spot_references.services.SportTypeService;
import com.shulpov.spots_app.spot_references.services.SpotTypeService;
import com.shulpov.spots_app.spot_user_infos.models.SpotUser;
import com.shulpov.spots_app.spot_user_infos.dto.SpotUserDto;
import com.shulpov.spots_app.spot_user_infos.SpotUserService;
import com.shulpov.spots_app.spots.models.Spot;
import com.shulpov.spots_app.spots.dto.SpotDto;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Класс, помогающий конвертировать модели в их DTO и обратно. Класс-@Component.
 * @author Victor Shulpov "vshulpov@gmail.com"
 */
@Component
public class DtoConverter {
    private final Logger logger = LoggerFactory.getLogger(DtoConverter.class);

    private final  ModelMapper modelMapper;
    private final ImageInfoDtoConverter imageInfoDtoConverter;
    private final SpaceTypeService spaceTypeService;
    private final SpotTypeService spotTypeService;
    private final SportTypeService sportTypeService;
    private final SpotUserService spotUserService;

    @Autowired
    public DtoConverter(@Lazy ModelMapper modelMapper, ImageInfoDtoConverter imageInfoDtoConverter, @Lazy SpaceTypeService spaceTypeService,
                        @Lazy SpotTypeService spotTypeService, @Lazy SportTypeService sportTypeService,
                        SpotUserService spotUserService) {
        this.modelMapper = modelMapper;
        this.imageInfoDtoConverter = imageInfoDtoConverter;
        this.spaceTypeService = spaceTypeService;
        this.spotTypeService = spotTypeService;
        this.sportTypeService = sportTypeService;
        this.spotUserService = spotUserService;
    }

    /**
     * Конвертор класса SpotDto в класс Spot
     * @param dto класс DTO создаваемого спота
     * @return новый экземпляр класса спота
     */
    public Spot dtoToNewSpot(SpotDto dto) throws NoSuchElementException {
        logger.atInfo().log("dtoToNewSpot name: {}", dto.getName());
        Spot spot = modelMapper.map(dto, Spot.class);
        spot.setAccepted(false);
        spot.setAddingDate(new Date(System.currentTimeMillis()));
        spot.setUpdatingDate(new Date(System.currentTimeMillis()));

        Optional<SpaceType> spaceTypeOpt = spaceTypeService.getById(dto.getSpaceTypeId());
        if(spaceTypeOpt.isPresent()) {
            spot.setSpaceType(spaceTypeOpt.get());
        } else {
            logger.atInfo().log("dtoToNewSpot Space type with such id not found, id={}", dto.getSpaceTypeId());
            throw new NoSuchElementException("Space type with such id not found");
        }

        List<SpotType> spotTypes = spotTypeService.getByIds(dto.getSpotTypeIds());
        spot.setSpotTypes(spotTypes);

        List<SportType> sportTypes = sportTypeService.getByIds(dto.getSportTypeIds());
        spot.setSportTypes(sportTypes);

        spotTypeService.getByIds(dto.getSpotTypeIds());

        return spot;
    }

    /**
     * Конвертор класса Spot в класс SpotDto
     * @param spot класс спота
     * @return класс DTO спота
     */
    public SpotDto spotToDto(Spot spot) {
        logger.atInfo().log("spotToDto id: {}, name: {}", spot.getId(), spot.getName());
        SpotDto dto = new SpotDto();
        dto.setId(spot.getId());
        dto.setAccepted(spot.getAccepted());
        dto.setLatitude(spot.getLatitude());
        dto.setLongitude(spot.getLongitude());
        dto.setDescription(spot.getDescription());
        dto.setName(spot.getName());
        dto.setAddingDate(spot.getAddingDate());
        dto.setUpdatingDate(spot.getUpdatingDate());
        dto.setSpotTypeIds(spot.getSpotTypes().stream().map(SpotType::getId).toList());
        dto.setSportTypeIds(spot.getSportTypes().stream().map(SportType::getId).toList());
        dto.setSpaceTypeId(spot.getSpaceType().getId());
        //инф о картинках
        List<ImageInfoDto> imageInfoDtoList = spot.getImageInfos().stream()
                .map(imageInfoDtoConverter::imageInfoToDto).toList();
        dto.setImageInfoDtoList(imageInfoDtoList);
        //кол-во избранных
        Integer favoriteNum = spotUserService.getFavoriteNumber(spot);
        dto.setFavoriteNumber(favoriteNum);
        //кол-во лайков
        Integer likeNum = spotUserService.getLikeNumber(spot);
        dto.setLikeNumber(likeNum);

        return dto;
    }

    /**
     * Конвертор класса SportType в класс SportTypeDto
     * @param sportType класс типа спорта
     * @return класс DTO типа спорта
     */
    public SportTypeDto sportTypeToDto(SportType sportType) {
        logger.atInfo().log("sportTypeToDto id={}", sportType.getId());
        return modelMapper.map(sportType, SportTypeDto.class);
    }

    /**
     * Конвертор класса SpotType в класс SpotTypeDto
     * @param spotType класс типа спота
     * @return класс DTO типа спота
     */
    public SpotTypeDto spotTypeToDto(SpotType spotType) {
        logger.atInfo().log("spotTypeToDto id={}", spotType.getId());
        return modelMapper.map(spotType, SpotTypeDto.class);
    }

    /**
     * Конвертор класса SpaceType в класс SpaceTypeDto
     * @param spaceType класс типа помещения
     * @return класс DTO типа помещения
     */
    public SpaceTypeDto spaceTypeToDto(SpaceType spaceType) {
        logger.atInfo().log("spaceTypeToDto id={}", spaceType.getId());
        return modelMapper.map(spaceType, SpaceTypeDto.class);
    }

    /**
     * Конвертор класса SpotUser в класс SpotUserDto
     * @param spotUser класс данных между спотами и пользователями
     * @return класс DTO данных между спотами и пользователями
     */
    public SpotUserDto spotUserToDto(SpotUser spotUser) {
        logger.atInfo().log("spotUserToDto spot_id={}, user_id={}",
                spotUser.getPostedSpot().getId(), spotUser.getUserActor().getId());
        SpotUserDto dto = modelMapper.map(spotUser, SpotUserDto.class);
        dto.setSpotId(spotUser.getPostedSpot().getId());
        dto.setUserId(spotUser.getUserActor().getId());
        return dto;
    }

    /**
     * Преобразует объект FieldError (ошибка валидации поля) в объект FieldErrorDto.
     *
     * @param fieldError Объект FieldError, представляющий ошибку валидации поля.
     * @return Объект FieldErrorDto, содержащий информацию об ошибке в удобном для передачи формате.
     * Пример использования:
     * <pre>
     * FieldError fieldError = new FieldError("user", "username", "Имя пользователя уже занято");
     * FieldErrorDto fieldErrorDto = fieldErrorToDto(fieldError);
     * </pre>
     */
    public FieldErrorDto fieldErrorToDto(FieldError fieldError) {
        logger.atInfo().log("fieldErrorToDto rejectedValue={}, message={}",
                fieldError.getRejectedValue(), fieldError.getDefaultMessage());
        return modelMapper.map(fieldError, FieldErrorDto.class);
    }

    /**
     * Конвертор класса Country в класс CountryDto
     * @param country класс страны
     * @return класс DTO класса страны
     */
    public CountryDto countryToDto(Country country) {
        return modelMapper.map(country, CountryDto.class);
    }

    /**
     * Конвертор класса Region в класс RegionDto
     * @param region класс региона
     * @return класс DTO класса региона
     */
    public RegionDto regionToDto(Region region) {
        return modelMapper.map(region, RegionDto.class);
    }

    /**
     * Конвертор класса City в класс CityDto
     * @param city класс города
     * @return класс DTO класса города
     */
    public CityDto cityToDto(City city) {
        return modelMapper.map(city, CityDto.class);
    }
}
