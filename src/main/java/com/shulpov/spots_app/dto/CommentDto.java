package com.shulpov.spots_app.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import java.util.Date;

public class CommentDto {
    @NotBlank(message = "Комментарий не должен быть пустым")
    @Size(max = 100, message = "Длина комментария не должна быть больше 100 символов")
    private String text;

    @Column(name = "upload_date")
    private Date uploadDate;

    private UserDto commentatorDto;

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

    public UserDto getCommentatorDto() {
        return commentatorDto;
    }

    public void setCommentatorDto(UserDto commentatorDto) {
        this.commentatorDto = commentatorDto;
    }
}
