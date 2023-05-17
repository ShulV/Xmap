package com.shulpov.spots_app.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import java.util.Date;

@Entity
@Table(name = "comments")
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

    public Spot getCommentedSpot() {
        return commentedSpot;
    }

    public void setCommentedSpot(Spot commentedSpot) {
        this.commentedSpot = commentedSpot;
    }

    public User getCommentator() {
        return commentator;
    }

    public void setCommentator(User commentator) {
        this.commentator = commentator;
    }
}
