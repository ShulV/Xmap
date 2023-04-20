package com.shulpov.spots_app.repo;

import com.shulpov.spots_app.models.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends CrudRepository<Role, Long> {
    Role findByNameCode(String user_role);
}
