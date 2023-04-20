package com.shulpov.spots_app.models;

import jakarta.persistence.*;

@Entity
@Table(name = "image_info")
public class ImageInfo {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
