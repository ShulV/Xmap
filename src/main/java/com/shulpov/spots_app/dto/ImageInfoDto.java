package com.shulpov.spots_app.dto;

import com.shulpov.spots_app.models.Spot;
import com.shulpov.spots_app.models.User;
import jakarta.persistence.*;

import java.time.LocalDate;

public class ImageInfoDto {
        private String originalName;

        private String genName;

        private int size;

        private LocalDate uploadDate;

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
}

