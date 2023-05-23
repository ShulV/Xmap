package com.shulpov.spots_app.controllers;

import com.shulpov.spots_app.dto.AuthenticationDto;
import com.shulpov.spots_app.dto.UserDto;
import com.shulpov.spots_app.models.User;
import com.shulpov.spots_app.security.JWTUtil;
import com.shulpov.spots_app.services.RegistrationService;
import com.shulpov.spots_app.services.RoleService;
import com.shulpov.spots_app.services.UserService;
import com.shulpov.spots_app.utils.DtoConverter;
import com.shulpov.spots_app.utils.validators.UserValidator;
import jakarta.validation.Valid;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JWTUtil jwtUtil;
    private final UserValidator userValidator;
    private final RegistrationService registrationService;
    private final RoleService roleService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    private final DtoConverter dtoConverter;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    public AuthController(JWTUtil jwtUtil, UserValidator userValidator, RegistrationService registrationService,
                          RoleService roleService, UserService userService,
                          AuthenticationManager authenticationManager, DtoConverter dtoConverter) {
        this.jwtUtil = jwtUtil;
        this.userValidator = userValidator;
        this.registrationService = registrationService;
        this.roleService = roleService;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.dtoConverter = dtoConverter;
    }

//    @GetMapping("/login")
//    public String loginPage() {
//        return "auth/login";
//    }

    @PostMapping("/register")
    public Map<String, String> performRegistration(@RequestBody @Valid UserDto userDTO,
                                      BindingResult bindingResult) {
        logger.atInfo().log("/auth/register name={} email={} password={} phoneNumber={} birthday={}",
                userDTO.getName(),
                userDTO.getEmail(),
                userDTO.getPassword(),
                userDTO.getPhoneNumber(),
                userDTO.getBirthday());
        User user = dtoConverter.dtoToNewUser(userDTO);
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            logger.atInfo().log("reg validation error");
            Map<String, String> errorMap = new HashMap<>();
            List<ObjectError> errors = bindingResult.getAllErrors();
            errors.forEach(er-> errorMap.put(er.getObjectName(), er.getDefaultMessage()));
            logger.atInfo().log(errorMap.toString());
            return errorMap;
            //TODO сделать исключение или придумать как возвращать ошибку REST API
            //надо выбрасывать исключение, ловить его с помощью HandleException как в проекте N 3
        }
        User createdUser = registrationService.register(user);

        String token = jwtUtil.generateToken(user);
        logger.atInfo().log("/auth/register success token={}", token);
        return Map.of("jwtToken", token, "id", createdUser.getId().toString());
        //TODO возвращать response со статус успешным кодом (нужно возвращать еще токен)
    }

    @PostMapping("/login")
    public Map<String, String> performLogin(@RequestBody AuthenticationDto authenticationDTO) {
        logger.atInfo().log("/auth/login email={} password={}",
                authenticationDTO.getEmail(), authenticationDTO.getPassword());
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
            logger.atInfo().log("/auth/login success token={}", token);

            return Map.of("jwtToken", token, "id", userOpt.get().getId().toString());
        } else {
            logger.atError().log("/auth/login user with such email not found");
            return Map.of("error", "Пользователь с такими данными не найден");
        }

    }
}
