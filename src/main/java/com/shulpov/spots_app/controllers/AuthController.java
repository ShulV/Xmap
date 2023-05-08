package com.shulpov.spots_app.controllers;

import com.shulpov.spots_app.dto.AuthenticationDto;
import com.shulpov.spots_app.dto.UserDto;
import com.shulpov.spots_app.models.User;
import com.shulpov.spots_app.security.JWTUtil;
import com.shulpov.spots_app.services.RegistrationService;
import com.shulpov.spots_app.services.RoleService;
import com.shulpov.spots_app.services.UserService;
import com.shulpov.spots_app.utils.UserValidator;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JWTUtil jwtUtil;
    private final UserValidator userValidator;
    private final ModelMapper modelMapper;
    private final RegistrationService registrationService;
    private final RoleService roleService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    private final static Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    public AuthController(JWTUtil jwtUtil, UserValidator userValidator, ModelMapper modelMapper, RegistrationService registrationService, RoleService roleService, UserService userService, AuthenticationManager authenticationManager) {
        this.jwtUtil = jwtUtil;
        this.userValidator = userValidator;
        this.modelMapper = modelMapper;
        this.registrationService = registrationService;
        this.roleService = roleService;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

//    @GetMapping("/login")
//    public String loginPage() {
//        return "auth/login";
//    }

    @PostMapping("/register")
    public Map<String, String> performRegistration(@RequestBody @Valid UserDto userDTO,
                                      BindingResult bindingResult) {
        logger.atInfo().log("/auth/register userDto.name={}", userDTO.getName());
        User user = convertToUser(userDTO);
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            logger.atInfo().log("reg validation error");
            Map<String, String> errorMap = new HashMap<>();
            List<ObjectError> errors = bindingResult.getAllErrors();
            errors.forEach((er)->{
                errorMap.put(er.getObjectName(), er.getDefaultMessage());
            });
            return errorMap;
            //TODO сделать исключение или придумать как возвращать ошибку REST API
            //надо выбрасывать исключение, ловить его с помощью HandleException как в проекте N 3
        }
        registrationService.register(user);

        String token = jwtUtil.generateToken(user);
        logger.atInfo().log("/auth/register success");
        return Map.of("jwtToken", token);
        //TODO возвращать response со статус успешным кодом (нужно возвращать еще токен)
    }

    @PostMapping("/login")
    public Map<String, String> performLogin(@RequestBody AuthenticationDto authenticationDTO) {
        logger.atInfo().log("/auth/login userDto.email={}", authenticationDTO.getEmail());
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(authenticationDTO.getEmail(),
                        authenticationDTO.getPassword());

        try {
            authenticationManager.authenticate(authInputToken);
        } catch (BadCredentialsException e) {
            logger.atInfo().log("/auth/login bad credentials");
            return Map.of("message", "Incorrect credentials!");
        }
        Optional<User> userOpt = userService.findByEmail(authenticationDTO.getEmail());
        if(userOpt.isPresent()) {
            String token = jwtUtil.generateToken(userOpt.get());
            logger.atInfo().log("/auth/login success");

            return Map.of("jwtToken", token);
        } else {
            logger.atError().log("/auth/login user with such email not found");
            return Map.of("error", "Пользователь с такими данными не найден");
        }

    }

    public User convertToUser(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        user.setRegDate(LocalDate.now());
        user.setRole(roleService.getUserRole());
        return user;
    }
}
