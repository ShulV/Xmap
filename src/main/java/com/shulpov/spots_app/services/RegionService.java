package com.shulpov.spots_app.services;

import com.shulpov.spots_app.models.Region;
import com.shulpov.spots_app.repo.RegionRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Scope(value = "prototype")
public class RegionService {
    private final RegionRepo regionRepo;

    private final Logger logger = LoggerFactory.getLogger(RegionService.class);

    public RegionService(RegionRepo regionRepo) {
        this.regionRepo = regionRepo;
    }

    public List<Region> getAll() {
        logger.atInfo().log("getAll");
        return regionRepo.findAll();
    }

    //Получить регион по id
    public Optional<Region> getById(Integer id) {
        logger.atInfo().log("getById id={}", id);
        return regionRepo.findById(id);
    }
}
