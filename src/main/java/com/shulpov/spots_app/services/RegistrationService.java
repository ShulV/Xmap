package com.shulpov.spots_app.services;

import com.shulpov.spots_app.models.Role;
import com.shulpov.spots_app.models.User;
import com.shulpov.spots_app.repo.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@Scope(value = "prototype")
public class RegistrationService {

    private final UserRepo userRepo;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(RegistrationService.class);

    @Autowired
    public RegistrationService(UserRepo userRepo, @Lazy RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User register(User user) {
        logger.atInfo().log("register id={} name={} email={} " +
                        "password={} phoneNumber={} role={} birthday={} regDate={}",
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getPhoneNumber(),
                user.getRoleCodeName(),
                user.getBirthday(),
                user.getRegDate());
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassHash(encodedPassword);
        Role userRole = roleService.getUserRole();
        user.setRole(userRole);
        return userRepo.save(user);
    }


}

