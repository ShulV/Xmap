package com.shulpov.spots_app.dto;

import jakarta.validation.constraints.Size;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import java.util.Date;

public class CommentDto {
    private Long id;

    @NotBlank(message = "Комментарий не должен быть пустым")
    @Size(max = 100, message = "Длина комментария не должна быть больше 100 символов")
    private String text;

    private Date uploadDate;

    private UserWithoutSpotsDto commentatorDto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public UserWithoutSpotsDto getCommentatorDto() {
        return commentatorDto;
    }

    public void setCommentatorDto(UserWithoutSpotsDto commentatorDto) {
        this.commentatorDto = commentatorDto;
    }
}
