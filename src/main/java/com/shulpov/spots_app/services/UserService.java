package com.shulpov.spots_app.services;

import com.shulpov.spots_app.models.User;
import com.shulpov.spots_app.repo.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Scope(value = "prototype")
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepo userRepo;

    @Autowired
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    //Найти пользователя по email
    public Optional<User> findByEmail(String email) {
        logger.atInfo().log("findByEmail email={}", email);
        return userRepo.findByEmail(email);
    }

    //Найти пользователя по имени
    public Optional<User> findByName(String name) {
        logger.atInfo().log("findByName name={}", name);
        return userRepo.findByName(name);
    }

    //Удалить пользователя по id
    @Transactional
    public void deleteById(Long id) {
        logger.atInfo().log("deleteById id={}", id);
        userRepo.deleteById(id);
    }

    //Найти пользователя по номеру телефона
    public Optional<User> findByPhoneNumber(String phoneNumber) {
        logger.atInfo().log("findByPhoneNumber phoneNumber={}", phoneNumber);
        return userRepo.findByPhoneNumber(phoneNumber);
    }

}
