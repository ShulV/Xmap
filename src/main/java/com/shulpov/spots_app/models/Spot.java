package com.shulpov.spots_app.models;

import jakarta.persistence.*;

@Entity
@Table(name = "spots")
public class Spot {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
