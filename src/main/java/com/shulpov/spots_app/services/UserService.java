package com.shulpov.spots_app.services;

import com.shulpov.spots_app.models.User;
import com.shulpov.spots_app.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepo userRepo;

    @Autowired
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public void addUser(String name) {
        userRepo.save(new User(name));
    }

    public Optional<User> getUserById(Long id) {
        return userRepo.findById(id);
    }

    public Optional<User> findByLogin(String username) {
        return userRepo.findByEmail(username);
    }
}
