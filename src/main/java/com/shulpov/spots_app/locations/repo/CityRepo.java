package com.shulpov.spots_app.locations.repo;

import com.shulpov.spots_app.locations.models.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepo extends JpaRepository<City, Integer> {
    @Query("""
           SELECT c FROM City c 
           LEFT JOIN Region r ON c.regionId = r.id 
           WHERE c.regionId=:id
           """)
    List<City> findByRegionId(Integer id);

    @Query("""
           SELECT ci FROM City ci 
           LEFT JOIN Region r ON ci.regionId = r.id 
           LEFT JOIN Country co ON r.countryId = co.id
           WHERE r.countryId=:id
           """)
    List<City> findByCountryId(Integer id);
}