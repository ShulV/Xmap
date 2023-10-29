package com.shulpov.spots_app.locations.models;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "countries")
@Builder
public class Country {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    String name;

    @OneToMany(mappedBy = "country")
    private List<Region> regions;

    public Country() {
    }

    public Country(Integer id, String name, List<Region> regions) {
        this.id = id;
        this.name = name;
        this.regions = regions;
    }
}