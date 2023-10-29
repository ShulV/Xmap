package com.shulpov.spots_app.spots;

import com.shulpov.spots_app.spots.models.Spot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpotRepo extends JpaRepository<Spot, Long> {
    //получить споты в заданном радиусе (ед. изм. - км)
    @Query(nativeQuery = true, value="SELECT * FROM get_spots_in_radius(:lat, :lon, :radius)")
    List<Spot> findSpotsInRadius(Double lat, Double lon, Double radius);
}
