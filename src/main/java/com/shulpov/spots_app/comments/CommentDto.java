package com.shulpov.spots_app.comments;

import com.shulpov.spots_app.users.dto.MainUserInfoDto;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import java.util.Date;

/**
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
@Getter
@Setter
public class CommentDto {

    private Long id;

    @NotBlank(message = "Комментарий не должен быть пустым")
    @Size(max = 100, message = "Длина комментария не должна быть больше 100 символов")
    private String text;

    private Date uploadDate;

    private MainUserInfoDto commentatorDto;
}
