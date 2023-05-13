package com.shulpov.spots_app.dto;

import java.time.LocalDate;

public class ImageInfoDto {
        private String url;

        private int size;

        private LocalDate uploadDate;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

