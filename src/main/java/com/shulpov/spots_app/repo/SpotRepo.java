package com.shulpov.spots_app.repo;

import com.shulpov.spots_app.models.Spot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpotRepo extends JpaRepository<Spot, Long> {
}
