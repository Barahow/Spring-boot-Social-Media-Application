package com.example.dev.social.media.service;

import com.example.dev.social.media.model.AppUser;
import com.example.dev.social.media.model.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {


    AppUser createUser(AppUser user);

    AppUser getUser(String email);

    UserRole saveRole(UserRole role);

    Page<AppUser> getAllUsers(Pageable pageable);

    void deleteUser(AppUser user);

    void addRoleToUser(String email, String roleName);
}
