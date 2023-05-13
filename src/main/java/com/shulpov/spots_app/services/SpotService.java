package com.shulpov.spots_app.services;

import com.shulpov.spots_app.models.Spot;
import com.shulpov.spots_app.repo.SpotRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpotService {

    private final SpotRepo spotRepo;

    private final static Logger logger = LoggerFactory.getLogger(SpotService.class);

    @Autowired
    public SpotService(SpotRepo spotRepo) {
        this.spotRepo = spotRepo;
    }

    //Получить все споты
    public List<Spot> getAllSpots() {
        List<Spot> spots = spotRepo.findAll();
        logger.atInfo().log("getAllSpots() size={}", spots.size());
        return spots;
    }
}
