package com.shulpov.spots_app.controllers;

import com.shulpov.spots_app.models.PersonDetails;
import com.shulpov.spots_app.models.User;
import com.shulpov.spots_app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class MainController {

    @GetMapping()
    public String getMessage() {
        return "Оно работает!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! v3 + фича 1 + фича 2";
    }

}
