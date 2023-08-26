package com.shulpov.spots_app.utils;

import com.shulpov.spots_app.dto.*;
import com.shulpov.spots_app.models.*;
import com.shulpov.spots_app.services.*;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Класс, помогающий конвертировать модели в их DTO и обратно. Класс-@Component.
 * @author Victor Shulpov "vshulpov@gmail.com"
 * @version 1.0
 * @since 1.0
 */
@Component
public class DtoConverter {

    private final Logger logger = LoggerFactory.getLogger(DtoConverter.class);

    /** Компонент маппинга классов */
    private final  ModelMapper modelMapper;

    /** Сервис ролей */
    private final  RoleService roleService;

    /** Сервис типов помещений */
    private final  SpaceTypeService spaceTypeService;
    /** Сервис типов спотов */
    private final  SpotTypeService spotTypeService;
    /** Сервис типов спорта */
    private final  SportTypeService sportTypeService;
    /** Сервис управления данными между пользователями и спотами */
    private final SpotUserService spotUserService;

    /**
     * Конструктор компонента
     * @param modelMapper компонент маппинга классов
     * @param roleService сервис ролей
     * @param spaceTypeService сервис типов помещений
     * @param spotTypeService сервис типов спотов
     * @param sportTypeService сервис типов спорта
     * @param spotUserService сервис управления данными между пользователями и спотами
     */

    @Autowired
    public DtoConverter(@Lazy ModelMapper modelMapper, @Lazy RoleService roleService,
                        @Lazy SpaceTypeService spaceTypeService, @Lazy SpotTypeService spotTypeService,
                        @Lazy SportTypeService sportTypeService, SpotUserService spotUserService) {
        this.modelMapper = modelMapper;
        this.roleService = roleService;
        this.spaceTypeService = spaceTypeService;
        this.spotTypeService = spotTypeService;
        this.sportTypeService = sportTypeService;
        this.spotUserService = spotUserService;
    }

    /**
     * Конвертор класса User в класс UserDto
     * @param user класс пользователя
     * @return класс DTO пользователя
     */
    public UserDto userToDto(User user) {
        logger.atInfo().log("userToDto id: {}, name: {}", user.getId(), user.getName());
        UserDto dto = modelMapper.map(user, UserDto.class);
        dto.setImageInfoDtoList(user.getImageInfos().stream().map(this::imageInfoToDto).toList());
        dto.setCreatedSpots(user.getCreatedSpots().stream().map(this::spotToDto).toList());
        dto.setLikedSpotIds(spotUserService.getLikedSpotUsers(user).stream()
                .map(ss -> ss.getPostedSpot().getId()).toList());
        dto.setFavoriteSpotIds(spotUserService.getFavoriteSpotUsers(user).stream()
                .map(ss -> ss.getPostedSpot().getId()).toList());
        return dto;
    }

    /**
     * Конвертор класса UserDto в класс User
     * @param dto класс DTO пользователя
     * @return класс пользователя
     */
    public User dtoToNewUser(UserDto dto) {
        logger.atInfo().log("dtoToNewUser name: {}", dto.getName());
        User user = modelMapper.map(dto, User.class);
        user.setRegDate(new Date(System.currentTimeMillis()));
        user.setRole(roleService.getUserRole());
        return user;
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
                .map(this::imageInfoToDto).toList();
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
     * Конвертор класса ImageInfo в класс ImageInfoDto
     * @param imageInfo класс информации об изображении
     * @return класс DTO информации об изображении
     */
    public ImageInfoDto imageInfoToDto(ImageInfo imageInfo) throws NullPointerException {
        logger.atInfo().log("imageInfoToDto id: {}, genName: {}", imageInfo.getId(), imageInfo.getGenName());
        ImageInfoDto dto = modelMapper.map(imageInfo, ImageInfoDto.class);

        if(imageInfo.getPhotographedUser() != null) {
            String url = ImageUtil.getUserImageDownloadUrl(imageInfo.getId());
            dto.setUrl(url);
            logger.atInfo().log("imageInfoToDto image for user, user_id={}", imageInfo.getPhotographedUser().getId());
        } else if(imageInfo.getPhotographedSpot() != null) {
            String url = ImageUtil.getSpotImageDownloadUrl(imageInfo.getId());
            dto.setUrl(url);
            logger.atInfo().log("imageInfoToDto image for spot, spot_id={}", imageInfo.getPhotographedSpot().getId());
        } else {
            logger.atInfo().log("Invalid image info object (user and spot are null)");
            throw new NullPointerException("Invalid image info object (user and spot are null)");
        }
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
     * Конвертор класса Comment в класс CommentDto
     * @param comment класс комментария
     * @return класс DTO комментария
     */
    public CommentDto commentToDto(Comment comment) {
        logger.atInfo().log("commentToDto id={}", comment.getId());
        CommentDto dto = modelMapper.map(comment, CommentDto.class);
        UserDto userDto = userToDto(comment.getCommentator());
        dto.setCommentatorDto(new UserWithoutSpotsDto(userDto));
        return dto;
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
     * Конвертор класса Country в класс CountryDto
     * @param country класс страны
     * @return класс DTO класса страны
     */
    public CountryDto countryToDto(Country country) {
        // TODO дописать логгер
        return modelMapper.map(country, CountryDto.class);
    }

    /**
     * Конвертор класса Region в класс RegionDto
     * @param region класс региона
     * @return класс DTO класса региона
     */
    public RegionDto regionToDto(Region region) {
        // TODO дописать логгер
        return modelMapper.map(region, RegionDto.class);
    }

    /**
     * Конвертор класса City в класс CityDto
     * @param city класс города
     * @return класс DTO класса города
     */
    public CityDto cityToDto(City city) {
        // TODO дописать логгер
        return modelMapper.map(city, CityDto.class);
    }
}
