package com.shulpov.spots_app.controllers;

import com.shulpov.spots_app.models.User;
import com.shulpov.spots_app.services.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final RegistrationService registrationService;

    @Autowired
    public AuthController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

//    @GetMapping("/login")
//    public String loginPage() {
//        return "auth/login";
//    }

    @PostMapping("/registration")
    public String performRegistration(@Valid User user,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "REGISTRATION ERROR";//TODO сделать исключение или придумать как возвращать ошибку REST API
        }
        registrationService.register(user);
        return "200 OK";//TODO возвращать response со статус успешным кодом
    }
}
