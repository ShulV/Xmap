package com.example.demo.services;

import com.example.demo.models.User;
import com.example.demo.repo.UserRepo;
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
}
