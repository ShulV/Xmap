package com.shulpov.spots_app.locations.models;

import com.shulpov.spots_app.spots.models.Spot;
import com.shulpov.spots_app.users.models.User;
import jakarta.persistence.*;
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
public class City {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "region_id", referencedColumnName = "id")
    private Region region;

    @OneToMany(mappedBy = "city")
    private List<User> users;

    @OneToMany(mappedBy = "city")
    private List<Spot> spots;
}
