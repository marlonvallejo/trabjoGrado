package com.hotelandinocode.hotelandino.service;

import com.hotelandinocode.hotelandino.entities.User;
import com.hotelandinocode.hotelandino.entities.UserRole;
import com.hotelandinocode.hotelandino.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {
    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        Set<GrantedAuthority> authorities = new HashSet<>();
        for (UserRole role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getRoleName().name()));
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }


    public boolean existsByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public User save(User user) {
        return this.userRepository.save(user);
    }

    public List<User> getAll() {
        return this.userRepository.findAll();
    }

    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<User> getUserById(Long id) {
        return this.userRepository.findById(id);
    }

    public Optional<User> getByUserName(String userName) {
        return this.userRepository.findByUsername(userName);
    }

}
