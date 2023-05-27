package com.example.dev.social.media.service;

import com.example.dev.social.media.model.AppUser;
import com.example.dev.social.media.model.UserRole;
import com.example.dev.social.media.repository.RoleRepository;
import com.example.dev.social.media.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@Transactional
@AllArgsConstructor

public class UserServiceImp implements UserService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


    @Override
    public AppUser createUser(AppUser user) {
        return null;
    }

    @Override
    public AppUser getUser(String email) {
        return null;
    }

    @Override
    public UserRole saveRole(UserRole role) {
        return null;
    }

    @Override
    public Page<AppUser> getAllUsers(Pageable pageable) {
        return null;
    }

    @Override
    public void deleteUser(AppUser user) {

    }

    @Override
    public void addRoleToUser(String email, String roleName) {

    }
}
