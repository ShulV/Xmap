package com.shulpov.spots_app.services;

import com.shulpov.spots_app.models.City;
import com.shulpov.spots_app.repo.CityRepo;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@Scope(value = "prototype")
public class CityService {
    private final CityRepo cityRepo;

    public CityService(CityRepo cityRepo) {
        this.cityRepo = cityRepo;
    }

    public List<City> getAll() {
        return cityRepo.findAll();
    }
}
