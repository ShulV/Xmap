package com.shulpov.spots_app.models;

import com.shulpov.spots_app.dto.ImageInfoDto;
import com.shulpov.spots_app.utils.ImageUtil;
import jakarta.persistence.*;

import java.util.Date;

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
    private Date uploadDate;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "spot_id", referencedColumnName = "id")
    private Spot spot;

    //to data transfer object
    public ImageInfoDto toDto() throws Exception {
        ImageInfoDto imageInfoDto = new ImageInfoDto();
        String url;
        if(this.spot != null && this.user == null) {
            url = ImageUtil.getSpotImageUrl(this.id);
        } else if(this.user != null && this.spot == null) {
            url = ImageUtil.getUserImageUrl(this.id);
        } else {
            throw new Exception("Invalid imageInfo object");
        }
        imageInfoDto.setUrl(url);
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

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
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
