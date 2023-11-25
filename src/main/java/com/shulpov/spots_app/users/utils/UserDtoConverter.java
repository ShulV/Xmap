package com.shulpov.spots_app.users.utils;

import com.shulpov.spots_app.common.converters.DtoConvertible;
import com.shulpov.spots_app.image_infos.utils.ImageInfoDtoConverter;
import com.shulpov.spots_app.users.dto.CommentatorDto;
import com.shulpov.spots_app.users.dto.UserDto;
import com.shulpov.spots_app.users.models.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
@Component
public class UserDtoConverter implements DtoConvertible<User, UserDto> {
    private final ModelMapper modelMapper;
    private final ImageInfoDtoConverter imageInfoDtoConverter;

    public UserDtoConverter(ModelMapper modelMapper, ImageInfoDtoConverter imageInfoDtoConverter) {
        this.modelMapper = modelMapper;
        this.imageInfoDtoConverter = imageInfoDtoConverter;
    }

    public CommentatorDto convertToCommentatorDto(User user) {
        CommentatorDto dto = modelMapper.map(user, CommentatorDto.class);
        if (!user.getImageInfos().isEmpty()) {
            dto.setCommentatorImageInfo(imageInfoDtoConverter.convertToDto(user.getImageInfos().get(0)));
        }
        return dto;
    }

    //не используется
    @Override
    public User convertToEntity(UserDto dto) {
        return null;
    }

    //не используется
    @Override
    public UserDto convertToDto(User entity) {
        return null;
    }
}
