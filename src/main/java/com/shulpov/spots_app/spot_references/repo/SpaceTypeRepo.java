package com.shulpov.spots_app.spot_references.repo;

import com.shulpov.spots_app.spot_references.models.SpaceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpaceTypeRepo extends JpaRepository<SpaceType, Integer> {
}
