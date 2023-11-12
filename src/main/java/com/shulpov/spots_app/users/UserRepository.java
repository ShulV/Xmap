package com.shulpov.spots_app.users;

import com.shulpov.spots_app.users.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByName(String name);
    Optional<User> findByPhoneNumber(String phoneNumber);
}
