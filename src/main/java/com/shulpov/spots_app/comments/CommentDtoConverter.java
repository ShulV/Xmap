package com.shulpov.spots_app.comments;

import com.shulpov.spots_app.common.converters.DtoConvertible;
import com.shulpov.spots_app.users.dto.CommentatorDto;
import com.shulpov.spots_app.users.utils.UserDtoConverter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
@Component
public class CommentDtoConverter implements DtoConvertible<Comment, CommentDto> {
    private final ModelMapper modelMapper;
    private final UserDtoConverter userDtoConverter;

    public CommentDtoConverter(ModelMapper modelMapper, UserDtoConverter userDtoConverter) {
        this.modelMapper = modelMapper;
        this.userDtoConverter = userDtoConverter;
    }

    //не используется
    @Override
    public Comment convertToEntity(CommentDto dto) {
        return null;
    }

    @Override
    public CommentDto convertToDto(Comment entity) {
        CommentDto dto = modelMapper.map(entity, CommentDto.class);
        CommentatorDto commentatorDto = userDtoConverter.convertToCommentatorDto(entity.getCommentator());
        dto.setCommentatorDto(commentatorDto);
        return dto;
    }
}
