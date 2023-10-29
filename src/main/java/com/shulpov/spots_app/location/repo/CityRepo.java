package com.shulpov.spots_app.location.repo;

import com.shulpov.spots_app.location.models.City;
import com.shulpov.spots_app.location.models.Country;
import com.shulpov.spots_app.location.models.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepo extends JpaRepository<City, Integer> {
    List<City> findByRegion(Region region);

    List<City> findByRegion_Country(Country country);

}