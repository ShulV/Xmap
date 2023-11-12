package com.shulpov.spots_app.comments;

import com.shulpov.spots_app.spots.models.Spot;
import com.shulpov.spots_app.users.models.User;
import jakarta.persistence.*;
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
@Entity
@Table(name = "comments")
@Getter
@Setter
public class Comment {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text")
    @NotBlank(message = "Комментарий не должен быть пустым")
    @Size(max = 100, message = "Длина комментария не должна быть больше 100 символов")
    private String text;

    @Column(name = "upload_date")
    private Date uploadDate;

    @ManyToOne
    @JoinColumn(name = "spot_id", referencedColumnName = "id")
    private Spot commentedSpot;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User commentator;
}
