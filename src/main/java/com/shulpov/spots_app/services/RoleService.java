package com.shulpov.spots_app.services;

import com.shulpov.spots_app.models.Role;
import com.shulpov.spots_app.repo.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@Scope(value = "prototype")
public class RoleService {
    private final RoleRepo roleRepo;

    @Autowired
    public RoleService(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    public Role getUserRole() {
        return roleRepo.findByNameCode("ROLE_USER");
    }
}
