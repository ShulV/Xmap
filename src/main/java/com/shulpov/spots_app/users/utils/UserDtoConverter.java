package com.shulpov.spots_app.users.utils;

import com.shulpov.spots_app.image_infos.utils.ImageInfoDtoConverter;
import com.shulpov.spots_app.users.dto.MainUserInfoDto;
import com.shulpov.spots_app.users.models.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


/**
 * Компонент, помогающий конвертировать модели класса User в DTO и обратно.
 * @author Victor Shulpov "vshulpov@gmail.com"
 */
@Component
public class UserDtoConverter {
    private final ModelMapper modelMapper;
    private final ImageInfoDtoConverter imageInfoDtoConverter;

    public UserDtoConverter(ModelMapper modelMapper, ImageInfoDtoConverter imageInfoDtoConverter) {
        this.modelMapper = modelMapper;
        this.imageInfoDtoConverter = imageInfoDtoConverter;
    }

    /**
     * Конвертор класса User в класс MainUserInfoDto
     * @param user класс пользователя
     * @return класс DTO пользователя с основными свойствами пользователя
     */
    public MainUserInfoDto userToUserMainInfoDto(User user) {
        MainUserInfoDto dto = modelMapper.map(user, MainUserInfoDto.class);
        dto.setImageInfoDtoList(user.getImageInfos().stream().map(imageInfoDtoConverter::imageInfoToDto).toList());
        return dto;
    }
}
