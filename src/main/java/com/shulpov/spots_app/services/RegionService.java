package com.shulpov.spots_app.services;

import com.shulpov.spots_app.models.Region;
import com.shulpov.spots_app.repo.RegionRepo;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@Scope(value = "prototype")
public class RegionService {
    private final RegionRepo regionRepo;

    public RegionService(RegionRepo regionRepo) {
        this.regionRepo = regionRepo;
    }

    public List<Region> getAll() {
        return regionRepo.findAll();
    }
}
