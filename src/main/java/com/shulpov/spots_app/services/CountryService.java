package com.shulpov.spots_app.services;

import com.shulpov.spots_app.models.Country;
import com.shulpov.spots_app.repo.CountryRepo;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@Scope(value = "prototype")
public class CountryService {

    private final CountryRepo countryRepo;

    public CountryService(CountryRepo countryRepo) {
        this.countryRepo = countryRepo;
    }

    public List<Country> getAll() {
        return countryRepo.findAll();
    }
}
