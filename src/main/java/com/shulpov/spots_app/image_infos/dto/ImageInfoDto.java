package com.shulpov.spots_app.image_infos.dto;

import java.io.Serializable;
import java.util.Date;

public class ImageInfoDto implements Serializable {

    private Long id;
    private String url;
    private int size;

    private Date uploadDate;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

