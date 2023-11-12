package com.shulpov.spots_app.spot_references.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shulpov.spots_app.spots.models.Spot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
@Entity
@Table(name = "sport_types")
@Getter
@Setter
public class SportType {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    String name;

    @Column(name = "transport_name")
    String transportName;

    @ManyToMany(mappedBy = "sportTypes",
            cascade = {CascadeType.PERSIST,CascadeType.MERGE })
    @JsonIgnore
    private List<Spot> spots;
}
