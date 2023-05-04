package com.shulpov.spots_app.services;

import com.shulpov.spots_app.models.PersonDetails;
import com.shulpov.spots_app.models.User;
import com.shulpov.spots_app.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonDetailsService implements UserDetailsService {

    private final UserRepo userRepo;

    @Autowired
    public PersonDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepo.findByEmail(email);

        if (user.isEmpty())
            throw new UsernameNotFoundException("User not found");

        return new PersonDetails(user.get());
    }
}

//@Service
//public class PersonDetailsService implements UserDetailsService {
//
//    private final UserService userService;
//
//    @Autowired
//    public PersonDetailsService(UserService userService) {
//        this.userService = userService;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return userService
//                .findByLogin(username)
//                .map(SecurityUser::new)
//                .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));
//    }
//
//    public Optional<User> getAuthenticatedUser() {
//       Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
//       return userService.findByLogin(authentication.getName());
//    }
//}
