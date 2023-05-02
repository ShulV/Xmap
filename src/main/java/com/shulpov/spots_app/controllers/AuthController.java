package com.shulpov.spots_app.controllers;

import com.shulpov.spots_app.dto.UserDTO;
import com.shulpov.spots_app.models.User;
import com.shulpov.spots_app.security.JWTUtil;
import com.shulpov.spots_app.services.RegistrationService;
import com.shulpov.spots_app.services.RoleService;
import com.shulpov.spots_app.utils.UserValidator;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JWTUtil jwtUtil;
    private final UserValidator userValidator;
    private final ModelMapper modelMapper;
    private final RegistrationService registrationService;

    private final RoleService roleService;

    @Autowired
    public AuthController(JWTUtil jwtUtil, UserValidator userValidator, ModelMapper modelMapper, RegistrationService registrationService, RoleService roleService) {
        this.jwtUtil = jwtUtil;
        this.userValidator = userValidator;
        this.modelMapper = modelMapper;
        this.registrationService = registrationService;
        this.roleService = roleService;
    }

//    @GetMapping("/login")
//    public String loginPage() {
//        return "auth/login";
//    }

    @PostMapping("/register")
    public Map<String, String> performRegistration(@RequestBody @Valid UserDTO userDTO,
                                      BindingResult bindingResult) {
        User user = convertToUser(userDTO);
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return Map.of("error_key", "error_value");
            //TODO сделать исключение или придумать как возвращать ошибку REST API
            //надо выбрасывать исключение, ловить его с помощью HandleException как в проекте N 3
        }
        registrationService.register(user);

        String token = jwtUtil.generateToken(user.getEmail());
        return Map.of("jwtToken", token);
        //TODO возвращать response со статус успешным кодом (нужно возвращать еще токен)
    }

    public User convertToUser(UserDTO userDTO) {
        User user = this.modelMapper.map(userDTO, User.class);
        user.setRegDate(LocalDate.now());
        user.setRole(roleService.getUserRole());
        return user;
    }
}
