package com.shulpov.spots_app.repo;

import com.shulpov.spots_app.models.SpotType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpotTypeRepo extends JpaRepository<SpotType, Integer> {
}
