package com.shulpov.spots_app.utils;

import com.shulpov.spots_app.dto.*;
import com.shulpov.spots_app.models.*;
import com.shulpov.spots_app.services.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
public class DtoConverter {

    //TODO прологировать класс
    private static ModelMapper modelMapper;
    private static RoleService roleService;

    private static SpotService spotService;

    private static SpaceTypeService spaceTypeService;

    private static SpotTypeService spotTypeService;

    private static SportTypeService sportTypeService;

    @Autowired
    public DtoConverter(ModelMapper modelMapper, RoleService roleService,
                        SpotService spotService, SpaceTypeService spaceTypeService,
                        SpotTypeService spotTypeService, SportTypeService sportTypeService) {
        this.modelMapper = modelMapper;
        this.roleService = roleService;
        this.spotService = spotService;
        this.spaceTypeService = spaceTypeService;
        this.spotTypeService = spotTypeService;
        this.sportTypeService = sportTypeService;
    }

//    public static UserDto userToDto(User user) {
//        UserDto userDto = new UserDto();
//        //TODO ...
//        return userDto;
//    }

    public static User dtoToNewUser(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        user.setRegDate(new Date(System.currentTimeMillis()));
        user.setRole(roleService.getUserRole());
        return user;
    }

    public static Spot dtoToNewSpot(SpotDto dto) throws NoSuchElementException {
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

        //TODO images
        return spot;
    }

    public static SpotDto spotToDto(Spot spot) {
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

//    public static SportType dtoToSportType(SportTypeDto sportTypeDto) {
//        return modelMapper.map(sportTypeDto, SportType.class);
//    }

    public static SpotTypeDto spotTypeToDto(SpotType spotType) {
        return modelMapper.map(spotType, SpotTypeDto.class);
    }

//    public static SpotType dtoToSpotType(SpotTypeDto spotTypeDto) {
//        return modelMapper.map(spotTypeDto, SpotType.class);
//    }

    public static SpaceTypeDto spaceTypeToDto(SpaceType spaceType) {
        return modelMapper.map(spaceType, SpaceTypeDto.class);
    }

//    public static SpaceType dtoToSpaceType(SpaceTypeDto spaceTypeDto) {
//        return modelMapper.map(spaceTypeDto, SpaceType.class);
//    }
}
