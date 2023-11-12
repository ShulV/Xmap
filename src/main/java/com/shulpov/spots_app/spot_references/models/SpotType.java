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
@Table(name = "spot_types")
@Getter
@Setter
public class SpotType {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    String name;

    @ManyToMany(mappedBy = "spotTypes",
            fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST,CascadeType.MERGE })
    @JsonIgnore
    private List<Spot> spots;
}
