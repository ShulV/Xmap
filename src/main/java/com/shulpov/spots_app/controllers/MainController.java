package com.shulpov.spots_app.controllers;

import com.shulpov.spots_app.models.User;
import com.shulpov.spots_app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class MainController {

    private final UserService userService;

    @Autowired
    public MainController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String getHello() {
        return "Оно работает!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! v3 + фича 1 + фича 2";
    }

    @GetMapping("/addUser")
    public String addUser(@RequestParam String name) {
        userService.addUser(name);
        return "User " + name + " добавлен";
    }

    @GetMapping("/getUser/{id}")
    public String addUser(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isEmpty()) {
            return "User not found";
        }
        return "User: id=" + user.get().getId() + "; name="+user.get().getName();
    }
}
