package com.shulpov.spots_app.spot_references.repo;

import com.shulpov.spots_app.spot_references.models.SportType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
@Repository
public interface SportTypeRepo extends JpaRepository<SportType, Integer> {
}
