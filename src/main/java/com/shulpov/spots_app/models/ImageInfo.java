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

    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE })
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User photographedUser;

    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE })
    @JoinColumn(name = "spot_id", referencedColumnName = "id")
    private Spot photographedSpot;

//    //to data transfer object
//    public ImageInfoDto toDto() throws Exception {
//        ImageInfoDto imageInfoDto = new ImageInfoDto();
//        String url;
//        if(this.photographedSpot != null && this.photographedUser == null) {
//            url = ImageUtil.getSpotImageDownloadUrl(this.id);
//        } else if(this.photographedUser != null && this.photographedSpot == null) {
//            url = ImageUtil.getUserImageDownloadUrl(this.id);
//        } else {
//            throw new Exception("Invalid imageInfo object");
//        }
//        imageInfoDto.setUrl(url);
//        imageInfoDto.setSize(this.size);
//        imageInfoDto.setUploadDate(this.uploadDate);
//        return imageInfoDto;
//    }

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

    public User getPhotographedUser() {
        return photographedUser;
    }

    public void setPhotographedUser(User photographedUser) {
        this.photographedUser = photographedUser;
    }

    public Spot getPhotographedSpot() {
        return photographedSpot;
    }

    public void setPhotographedSpot(Spot photographedSpot) {
        this.photographedSpot = photographedSpot;
    }
}
