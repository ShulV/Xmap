package com.shulpov.spots_app.auth.controllers;

import com.shulpov.spots_app.auth.requests.AuthenticationRequest;
import com.shulpov.spots_app.auth.requests.RegisterRequest;
import com.shulpov.spots_app.auth.responses.AuthenticationResponse;
import com.shulpov.spots_app.auth.services.AuthenticationService;
import com.shulpov.spots_app.dto.FieldErrorDto;
import com.shulpov.spots_app.utils.DtoConverter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @author Shulpov Victor
 */

@RestController
@RequestMapping(value = "/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    @Autowired
    private final AuthenticationService service;

    @Autowired
    private final DtoConverter dtoConverter;

    @PostMapping(value="/register", consumes="application/json", produces = "application/json")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request, BindingResult errors
    ) {
        AuthenticationResponse response = service.register(request, errors);
        if(errors.hasErrors()) {
            List<FieldErrorDto> fieldErrors = errors.getFieldErrors()
                    .stream().map(dtoConverter::fieldErrorToDto)
                    .toList();
            return ResponseEntity.badRequest()
                    .body(fieldErrors);
        } else {
            return ResponseEntity.ok()
                    .body(response);
        }
    }

    @PostMapping(value="/authenticate", consumes="application/json", produces = "application/json")
    public ResponseEntity<?> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        //todo запилить свой auth provider, сделай response'ы удачные и не очень
        //todo (сделать аналогично register())
        //todo валидация количества попыток
        try {
            return ResponseEntity.ok(service.authenticate(request));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("errorMessage", e.getMessage()));
        }
    }

    @PostMapping(value="/refresh-token", produces = "application/json")
    public ResponseEntity<?> refreshToken(
            HttpServletRequest request
    ) {
        try {
            return service.refreshToken(request);
        } catch (AuthenticationCredentialsNotFoundException | NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("errorMessage", e.getMessage()));
        }
    }

}
