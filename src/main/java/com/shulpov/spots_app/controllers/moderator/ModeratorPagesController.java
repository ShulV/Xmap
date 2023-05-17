package com.shulpov.spots_app.controllers.moderator;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/moderator")
//@PreAuthorize("hasAnyRole('MODERATOR', 'ADMIN')")
public class ModeratorPagesController {

    @GetMapping("/auth/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/main")
    public String mainPage() {return "main";}
}
