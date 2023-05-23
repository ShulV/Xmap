package com.shulpov.spots_app.utils.validators;

import com.shulpov.spots_app.models.Spot;
import com.shulpov.spots_app.services.SpotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class SpotValidator implements Validator {

    private final SpotService spotService;
    private final Logger logger = LoggerFactory.getLogger(SpotValidator.class);

    @Autowired
    public SpotValidator(SpotService spotService) {
        this.spotService = spotService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Spot.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {

    }
}
