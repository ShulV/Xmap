package com.shulpov.spots_app.image_infos.utils;

import com.shulpov.spots_app.image_infos.dto.ImageInfoDto;
import com.shulpov.spots_app.image_infos.models.ImageInfo;
import com.shulpov.spots_app.common.utils.ImageUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ImageInfoDtoConverter {
    private final ModelMapper modelMapper;

    public ImageInfoDtoConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /**
     * Конвертор класса ImageInfo в класс ImageInfoDto
     */
    public ImageInfoDto imageInfoToDto(ImageInfo imageInfo) throws NullPointerException {
        ImageInfoDto dto = modelMapper.map(imageInfo, ImageInfoDto.class);

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
}
