package com.shulpov.spots_app.locations.repo;

import com.shulpov.spots_app.locations.models.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegionRepo extends JpaRepository<Region, Integer> {
    @Query("""
           SELECT r FROM Region r 
           LEFT JOIN Country c ON r.countryId = c.id 
           WHERE r.countryId=:id
           """)
    List<Region> findByCountryId(Integer id);
}
