package dev.social.media.security.service;

import dev.social.media.security.model.AppUser;
import dev.social.media.security.model.UserRole;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {


    AppUser createUser(AppUser user);

    AppUser getUser(String email);
    AppUser updateUser(AppUser user );

    UserRole saveRole(UserRole role);

    Page<AppUser> getAllUsers(Pageable pageable);

    void deleteUser(AppUser user);

    void addRoleToUser(String email, String roleName);

    Optional<AppUser> findById(ObjectId objectId);




}
