package com.shulpov.spots_app.spot_references.repo;

import com.shulpov.spots_app.spot_references.models.SpotType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
@Repository
public interface SpotTypeRepo extends JpaRepository<SpotType, Integer> {

    @Query("FROM SpotType WHERE id in :ids")
    List<SpotType> getByIds(List<Integer> ids);
}
