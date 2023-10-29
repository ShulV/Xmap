package com.shulpov.spots_app.locations.repo;

import com.shulpov.spots_app.locations.models.City;
import com.shulpov.spots_app.locations.models.Country;
import com.shulpov.spots_app.locations.models.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepo extends JpaRepository<City, Integer> {
    List<City> findByRegion(Region region);

    //hibernat'у нужно нижнее подчеркивание для того, чтобы не путать таблицу с полем (само генерится)
    List<City> findByRegion_Country(Country country);

}