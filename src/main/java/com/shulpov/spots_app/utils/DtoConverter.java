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

@Component
public class DtoConverter {

    private final Logger logger = LoggerFactory.getLogger(DtoConverter.class);
    private final  ModelMapper modelMapper;
    private final  RoleService roleService;
    private final  SpaceTypeService spaceTypeService;
    private final  SpotTypeService spotTypeService;
    private final  SportTypeService sportTypeService;
    private final SpotUserService spotUserService;


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

    public User dtoToNewUser(UserDto dto) {
        logger.atInfo().log("dtoToNewUser name: {}", dto.getName());
        User user = modelMapper.map(dto, User.class);
        user.setRegDate(new Date(System.currentTimeMillis()));
        user.setRole(roleService.getUserRole());
        return user;
    }

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

    public ImageInfoDto imageInfoToDto(ImageInfo imageInfo) throws NullPointerException {
        logger.atInfo().log("imageInfoToDto id: {}, genName: {}", imageInfo.getId(), imageInfo.getGenName());
        ImageInfoDto dto = new ImageInfoDto();
        dto.setSize(imageInfo.getSize());
        dto.setUploadDate(imageInfo.getUploadDate());
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

    public SportTypeDto sportTypeToDto(SportType sportType) {
        logger.atInfo().log("sportTypeToDto id={}", sportType.getId());
        return modelMapper.map(sportType, SportTypeDto.class);
    }

    public SpotTypeDto spotTypeToDto(SpotType spotType) {
        logger.atInfo().log("spotTypeToDto id={}", spotType.getId());
        return modelMapper.map(spotType, SpotTypeDto.class);
    }

    public SpaceTypeDto spaceTypeToDto(SpaceType spaceType) {
        logger.atInfo().log("spaceTypeToDto id={}", spaceType.getId());
        return modelMapper.map(spaceType, SpaceTypeDto.class);
    }

    public CommentDto commentToDto(Comment comment) {
        logger.atInfo().log("commentToDto id={}", comment.getId());
        CommentDto dto = modelMapper.map(comment, CommentDto.class);
        UserDto userDto = userToDto(comment.getCommentator());
        dto.setCommentatorDto(new UserWithoutSpotsDto(userDto));
        return dto;
    }

    public SpotUserDto spotUserToDto(SpotUser spotUser) {
        logger.atInfo().log("spotUserToDto spot_id={}, user_id={}",
                spotUser.getId().getSpotId(), spotUser.getId().getUserId());
        SpotUserDto dto = modelMapper.map(spotUser, SpotUserDto.class);
        dto.setSpotId(spotUser.getPostedSpot().getId());
        dto.setUserId(spotUser.getUserActor().getId());
        return dto;
    }

}
