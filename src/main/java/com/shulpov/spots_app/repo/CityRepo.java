package com.shulpov.spots_app.repo;

import com.shulpov.spots_app.models.City;
import com.shulpov.spots_app.models.Country;
import com.shulpov.spots_app.models.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepo extends JpaRepository<City, Integer> {
    List<City> findByRegion(Region region);

    List<City> findByRegion_Country(Country country);
}
