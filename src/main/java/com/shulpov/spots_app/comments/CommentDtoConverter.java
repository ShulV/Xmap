package com.shulpov.spots_app.comments;

import com.shulpov.spots_app.common.converters.DtoConvertible;
import com.shulpov.spots_app.users.dto.CommentatorDto;
import com.shulpov.spots_app.users.utils.UserDtoConverter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class CommentDtoConverter implements DtoConvertible<Comment, CommentDto> {
    private final ModelMapper modelMapper;
    private final UserDtoConverter userDtoConverter;

    @Override
    public CommentDto convertToDto(Comment entity) {
        CommentDto dto = modelMapper.map(entity, CommentDto.class);
        CommentatorDto commentatorDto = userDtoConverter.convertToCommentatorDto(entity.getCommentator());
        dto.setCommentatorDto(commentatorDto);
        return dto;
    }
}
