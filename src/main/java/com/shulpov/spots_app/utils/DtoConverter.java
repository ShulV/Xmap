package com.shulpov.spots_app.utils;

import com.shulpov.spots_app.dto.*;
import com.shulpov.spots_app.models.*;
import com.shulpov.spots_app.services.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class DtoConverter {

    private static ModelMapper modelMapper;
    private static RoleService roleService;

    @Autowired
    public DtoConverter(ModelMapper modelMapper, RoleService roleService) {
        this.modelMapper = modelMapper;
        this.roleService = roleService;
    }

    public static UserDto userToDto(User user) {
        UserDto userDto = new UserDto();
        //TODO ...
        return userDto;
    }

    public static User dtoToNewUser(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        user.setRegDate(LocalDate.now());
        user.setRole(roleService.getUserRole());
        return user;
    }

    public static Spot dtoToNewSpot(SpotDto spotDto) {
        Spot spot = modelMapper.map(spotDto, Spot.class);
        spot.setAccepted(false);
        spot.setAddingDate(LocalDate.now());
        spot.setUpdatingDate(LocalDate.now());
        //spot.setImageInfos();
        //TODO
        return spot;
    }

    public static SpotDto spotToDto(Spot spot) {
        SpotDto dto = new SpotDto();
        dto.setAccepted(spot.getAccepted());
        dto.setLatitude(spot.getLatitude());
        dto.setLongitude(spot.getLongitude());
        dto.setDescription(spot.getDescription());
        dto.setName(spot.getName());
        List<ImageInfoDto> imageInfoDtoList = spot.getImageInfos().stream()
                .map(DtoConverter::imageInfoToDto).toList();
        dto.setImageInfoDtoList(imageInfoDtoList);
        return dto;
    }

    public static ImageInfoDto imageInfoToDto(ImageInfo imageInfo) throws NullPointerException {
        ImageInfoDto dto = new ImageInfoDto();
        dto.setSize(imageInfo.getSize());
        dto.setUploadDate(imageInfo.getUploadDate());
        if(imageInfo.getUser() != null) {
            String url = ImageUtil.getUserImageUrl(imageInfo.getId());
            dto.setUrl(url);
        } else if(imageInfo.getSpot() != null) {
            String url = ImageUtil.getSpotImageUrl(imageInfo.getId());
            dto.setUrl(url);
        } else {
            throw new NullPointerException("Invalid image info object (user and spot are null)");
        }
        return dto;
    }

    public static SportTypeDto sportTypeToDto(SportType sportType) {
        return modelMapper.map(sportType, SportTypeDto.class);
    }

    public static SportType dtoToSportType(SportTypeDto sportTypeDto) {
        return modelMapper.map(sportTypeDto, SportType.class);
    }

    public static SpotTypeDto spotTypeToDto(SpotType spotType) {
        return modelMapper.map(spotType, SpotTypeDto.class);
    }

    public static SpotType dtoToSpotType(SpotTypeDto spotTypeDto) {
        return modelMapper.map(spotTypeDto, SpotType.class);
    }

    public static SpaceTypeDto spaceTypeToDto(SpaceType spaceType) {
        return modelMapper.map(spaceType, SpaceTypeDto.class);
    }

    public static SpaceType dtoToSpaceType(SpaceTypeDto spaceTypeDto) {
        return modelMapper.map(spaceTypeDto, SpaceType.class);
    }
}
