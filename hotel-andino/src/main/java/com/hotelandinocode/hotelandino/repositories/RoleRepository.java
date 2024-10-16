package com.hotelandinocode.hotelandino.repositories;

import com.hotelandinocode.hotelandino.entities.UserRole;
import com.hotelandinocode.hotelandino.enums.UserRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface RoleRepository extends JpaRepository<UserRole, Long> {
    Optional<UserRole> findByRoleName(UserRoleEnum userRoleEnum);
}
