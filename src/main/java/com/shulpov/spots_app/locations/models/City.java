package com.shulpov.spots_app.locations.models;

import com.shulpov.spots_app.spots.models.Spot;
import com.shulpov.spots_app.users.models.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Класс, представляющий город.
 */
@Getter
@Setter
@Entity
@Table(name = "cities")
@Builder
public class City {

    /**
     * Уникальный идентификатор города.
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Название города.
     */
    @Column(name = "name")
    private String name;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", referencedColumnName = "id")
    private Region region;

    @OneToMany(mappedBy = "city")
    private List<User> users;

    @OneToMany(mappedBy = "city")
    private List<Spot> spots;

    public City() {
    }

    public City(Integer id, String name, Region region, List<User> users, List<Spot> spots) {
        this.id = id;
        this.name = name;
        this.region = region;
        this.users = users;
        this.spots = spots;
    }
}
