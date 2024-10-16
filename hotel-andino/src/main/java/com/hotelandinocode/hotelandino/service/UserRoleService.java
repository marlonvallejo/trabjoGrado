package com.hotelandinocode.hotelandino.service;

import com.hotelandinocode.hotelandino.entities.UserRole;
import com.hotelandinocode.hotelandino.enums.UserRoleEnum;
import com.hotelandinocode.hotelandino.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserRoleService {
    private RoleRepository roleRepository;

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Optional<UserRole> getByRoleName(UserRoleEnum userRoleEnum) {
        return this.roleRepository.findByRoleName(userRoleEnum);
    }

    public void save(UserRole userRole) {
        this.roleRepository.save(userRole);
    }
}
