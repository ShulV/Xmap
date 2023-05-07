package com.shulpov.spots_app.models;

import com.shulpov.spots_app.dto.ImageInfoDto;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "image_info")
public class ImageInfo {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "orig_name")
    private String originalName;

    @Column(name = "gen_name")
    private String genName;

    @Column(name = "size")
    private int size;

    @Column(name = "upload_date")
    private LocalDate uploadDate;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "spot_id", referencedColumnName = "id")
    private Spot spot;

    public ImageInfoDto toDto() {
        ImageInfoDto imageInfoDto = new ImageInfoDto();
        imageInfoDto.setOriginalName(this.originalName);
        imageInfoDto.setGenName(this.genName);
        imageInfoDto.setSize(this.size);
        imageInfoDto.setUploadDate(this.uploadDate);
        return imageInfoDto;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getGenName() {
        return genName;
    }

    public void setGenName(String genName) {
        this.genName = genName;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public LocalDate getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDate uploadDate) {
        this.uploadDate = uploadDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Spot getSpot() {
        return spot;
    }

    public void setSpot(Spot spot) {
        this.spot = spot;
    }
}
