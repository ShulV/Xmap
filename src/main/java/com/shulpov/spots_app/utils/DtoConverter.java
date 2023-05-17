package com.shulpov.spots_app.utils;

import com.shulpov.spots_app.dto.*;
import com.shulpov.spots_app.models.*;
import com.shulpov.spots_app.services.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
public class DtoConverter {

    //TODO прологировать класс
    private final  ModelMapper modelMapper;
    private final  RoleService roleService;

    private final  SpotService spotService;

    private final  SpaceTypeService spaceTypeService;

    private final  SpotTypeService spotTypeService;

    private final  SportTypeService sportTypeService;


    @Autowired
    public DtoConverter(ModelMapper modelMapper, @Lazy RoleService roleService,
                        @Lazy SpotService spotService, @Lazy SpaceTypeService spaceTypeService,
                        @Lazy SpotTypeService spotTypeService, @Lazy SportTypeService sportTypeService) {
        this.modelMapper = modelMapper;
        this.roleService = roleService;
        this.spotService = spotService;
        this.spaceTypeService = spaceTypeService;
        this.spotTypeService = spotTypeService;
        this.sportTypeService = sportTypeService;
    }

    public UserDto userToDto(User user) {
        UserDto dto = modelMapper.map(user, UserDto.class);
        dto.setImageInfoDtoList(user.getImageInfos().stream().map(this::imageInfoToDto).toList());
        dto.setCreatedSpots(user.getCreatedSpots().stream().map(this::spotToDto).toList());
        return dto;
    }

    public User dtoToNewUser(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        user.setRegDate(new Date(System.currentTimeMillis()));
        user.setRole(roleService.getUserRole());
        return user;
    }

    public Spot dtoToNewSpot(SpotDto dto) throws NoSuchElementException {
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

    public SpotDto spotToDto(Spot spot) {
        SpotDto dto = new SpotDto();
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

        List<ImageInfoDto> imageInfoDtoList = spot.getImageInfos().stream()
                .map(this::imageInfoToDto).toList();
        dto.setImageInfoDtoList(imageInfoDtoList);
        return dto;
    }

    public ImageInfoDto imageInfoToDto(ImageInfo imageInfo) throws NullPointerException {
        ImageInfoDto dto = new ImageInfoDto();
        dto.setSize(imageInfo.getSize());
        dto.setUploadDate(imageInfo.getUploadDate());
        if(imageInfo.getPhotographedUser() != null) {
            String url = ImageUtil.getUserImageDownloadUrl(imageInfo.getId());
            dto.setUrl(url);
        } else if(imageInfo.getPhotographedSpot() != null) {
            String url = ImageUtil.getSpotImageDownloadUrl(imageInfo.getId());
            dto.setUrl(url);
        } else {
            throw new NullPointerException("Invalid image info object (user and spot are null)");
        }
        return dto;
    }

    public SportTypeDto sportTypeToDto(SportType sportType) {
        return modelMapper.map(sportType, SportTypeDto.class);
    }

//    public SportType dtoToSportType(SportTypeDto sportTypeDto) {
//        return modelMapper.map(sportTypeDto, SportType.class);
//    }

    public SpotTypeDto spotTypeToDto(SpotType spotType) {
        return modelMapper.map(spotType, SpotTypeDto.class);
    }

//    public SpotType dtoToSpotType(SpotTypeDto spotTypeDto) {
//        return modelMapper.map(spotTypeDto, SpotType.class);
//    }

    public SpaceTypeDto spaceTypeToDto(SpaceType spaceType) {
        return modelMapper.map(spaceType, SpaceTypeDto.class);
    }

//    public SpaceType dtoToSpaceType(SpaceTypeDto spaceTypeDto) {
//        return modelMapper.map(spaceTypeDto, SpaceType.class);
//    }

    public CommentDto commentToDto(Comment comment) {
        CommentDto dto = modelMapper.map(comment, CommentDto.class);
        dto.setCommentatorDto(userToDto(comment.getCommentator()));
        return dto;
    }

}
