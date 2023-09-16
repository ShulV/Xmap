package com.shulpov.spots_app.auth;

import com.shulpov.spots_app.dto.FieldErrorDto;
import com.shulpov.spots_app.utils.DtoConverter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * @author Shulpov Victor
 */

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    @Autowired
    private final AuthenticationService service;

    @Autowired
    private final DtoConverter dtoConverter;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request,  BindingResult errors
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

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        //todo запилить свой auth provider, сделай response'ы удачные и не очень
        //todo (сделать аналогично register())
        //todo валидация количества попыток
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        //todo (сделать аналогично register())
        service.refreshToken(request, response);
    }

}
