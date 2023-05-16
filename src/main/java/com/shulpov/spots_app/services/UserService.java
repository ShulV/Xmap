package com.shulpov.spots_app.services;

import com.shulpov.spots_app.models.User;
import com.shulpov.spots_app.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Scope(value = "prototype")
public class UserService {

    private final UserRepo userRepo;

    @Autowired
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

//    @Transactional
//    public void addUser(String name) {
//        userRepo.save(new User(name));
//    }

//    public Optional<User> getUserById(Long id) {
//        return userRepo.findById(id);
//    }

    public Optional<User> findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public Optional<User> findByName(String name) {
        return userRepo.findByName(name);
    }

    @Transactional
    public void deleteById(Long id) {
        userRepo.deleteById(id);
    }

    public Optional<User> findByPhoneNumber(String phoneNumber) {
        return userRepo.findByPhoneNumber(phoneNumber);
    }

}
