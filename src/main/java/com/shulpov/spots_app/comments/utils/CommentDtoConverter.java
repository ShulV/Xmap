package com.shulpov.spots_app.comments.utils;

import com.shulpov.spots_app.comments.Comment;
import com.shulpov.spots_app.comments.CommentDto;
import com.shulpov.spots_app.users.dto.MainUserInfoDto;
import com.shulpov.spots_app.users.utils.UserDtoConverter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
@Component
public class CommentDtoConverter {
    private final ModelMapper modelMapper;
    private final UserDtoConverter userDtoConverter;

    public CommentDtoConverter(ModelMapper modelMapper, UserDtoConverter userDtoConverter) {
        this.modelMapper = modelMapper;
        this.userDtoConverter = userDtoConverter;
    }

    public CommentDto commentToDto(Comment comment) {
        CommentDto dto = modelMapper.map(comment, CommentDto.class);
        MainUserInfoDto mainUserInfoDto = userDtoConverter.userToUserMainInfoDto(comment.getCommentator());
        dto.setCommentatorDto(mainUserInfoDto);
        return dto;
    }
}
