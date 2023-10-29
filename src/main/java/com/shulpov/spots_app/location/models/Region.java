package com.shulpov.spots_app.location.models;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "regions")
@Builder
public class Region {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", referencedColumnName = "id")
    private Country country;

    @OneToMany(mappedBy = "region")
    private List<City> cities;

    public Region() {
    }

    public Region(Integer id, String name, Country country, List<City> cities) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.cities = cities;
    }

}
