package com.shulpov.spots_app.spot_references.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shulpov.spots_app.spots.models.Spot;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "space_types")
public class SpaceType {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    String name;

    @OneToMany(mappedBy = "spaceType",
            cascade = {CascadeType.PERSIST,CascadeType.MERGE })
    @JsonIgnore
    private List<Spot> spots;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Spot> getSpots() {
        return spots;
    }

    public void setSpots(List<Spot> spots) {
        this.spots = spots;
    }
}
