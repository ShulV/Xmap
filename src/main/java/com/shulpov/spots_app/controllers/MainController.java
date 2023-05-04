package com.shulpov.spots_app.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping()
    public String getMessage() {
        return "Оно работает!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! v3 + фича 1 + фича 2";
    }

}
