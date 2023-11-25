package com.shulpov.spots_app.image_infos.utils;

import com.shulpov.spots_app.common.converters.DtoConvertible;
import com.shulpov.spots_app.image_infos.dto.ImageInfoDto;
import com.shulpov.spots_app.image_infos.models.ImageInfo;
import com.shulpov.spots_app.common.utils.ImageUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ImageInfoDtoConverter implements DtoConvertible<ImageInfo, ImageInfoDto> {
    private final ModelMapper modelMapper;

    public ImageInfoDtoConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    //не используется
    @Override
    public ImageInfo convertToEntity(ImageInfoDto dto) {
        return null;
    }

    @Override
    public ImageInfoDto convertToDto(ImageInfo entity) {
        ImageInfoDto dto = modelMapper.map(entity, ImageInfoDto.class);

        if(entity.getPhotographedUser() != null) {
            String url = ImageUtil.getUserImageDownloadUrl(entity.getId());
            dto.setUrl(url);
        } else if(entity.getPhotographedSpot() != null) {
            String url = ImageUtil.getSpotImageDownloadUrl(entity.getId());
            dto.setUrl(url);
        } else {
            throw new NullPointerException("Invalid image info object (user and spot are null)");
        }
        return dto;
    }
}
