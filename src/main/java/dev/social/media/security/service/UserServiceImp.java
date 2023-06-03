package dev.social.media.security.service;

import dev.social.media.security.config.PasswordEncoderConfig;
import dev.social.media.security.model.AppUser;
import dev.social.media.security.model.UserRole;
import dev.social.media.security.repository.RoleRepository;
import dev.social.media.security.repository.UserRepository;
import jakarta.validation.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;


@Slf4j
@Service
@Transactional
@AllArgsConstructor

public class UserServiceImp implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoderConfig passwordEncoderConfig;

    private final MongoTemplate mongoTemplate;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("email cannot be null or empty");
        }

        log.info("looking for user with email: {}", email);
        AppUser appUser = userRepository.findByEmailIgnoreCase(email);


        if (appUser == null) {
            throw new UsernameNotFoundException("email notfound in the database");
        }

        log.info("found user: {}", appUser);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        appUser.getRoleList().forEach(userRole -> {
            log.info("adding role: {},", userRole.getName());
            authorities.add(new SimpleGrantedAuthority(userRole.getName()));
        });

        return new org.springframework.security.core.userdetails.User(appUser.getEmail(), appUser.getPassword(), authorities);


    }


    @Override
    public AppUser createUser(AppUser user) {

        hashPassword(user);
        validateUser(user);


        AppUser newUser = userRepository.findByEmailIgnoreCase(user.getEmail());

        if (newUser != null) {
            throw new IllegalArgumentException("The email already exist");
        } else {
            Optional<UserRole> userRole = Optional.ofNullable(roleRepository.findByName("USER"));
            if (userRole.isEmpty()) {
                throw new IllegalArgumentException("Default user Role not found");

            }

            user.setRoleList(Collections.singletonList(userRole.get()));

            newUser = new AppUser(null, user.getUserName(), user.getEmail(), user.getPassword(), user.getCreateAt(), user.getUpdatedAt(), user.getAddress(), user.getFirstName(), user.getLastName(), user.getDateOfBirth(), user.getProfilePicture(), user.getRoleList());

            log.info("Created a new User {}", newUser);

            return userRepository.save(newUser);


        }
    }

    private void validateUser(AppUser user) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

        try {

            Validator validator = factory.getValidator();

            Set<ConstraintViolation<AppUser>> violations = validator.validate(user);

            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }


        } finally {
            factory.close();
        }


    }

    private void hashPassword(AppUser user) {
        user.setPassword(passwordEncoderConfig.passwordEncoder().encode(user.getPassword()));

    }

    @Override
    public AppUser getUser(String email) {
        // santize input by checking if the input is null or empty

        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("email cannnot be null");
        }
        log.info("fetching email {}", email);
        return userRepository.findByEmailIgnoreCase(email);
    }

    @Override
    public UserRole saveRole(UserRole role) {
        return roleRepository.save(role);
    }

    @Override
    public Page<AppUser> getAllUsers(Pageable pageable) {
        if (pageable == null) {
            throw new IllegalArgumentException("pagebale cannot be null");
        }
        log.info("fetching all users");


        return userRepository.findAll(pageable);


    }

    @Override
    public void deleteUser(AppUser user) {
        userRepository.delete(user);

    }

    @Override
    public void addRoleToUser(String email, String roleName) {

        if (email == null || email.isEmpty() || roleName == null || roleName.isEmpty()) {
            throw new IllegalArgumentException("username and role name should not be null");

        }

        UserRole userRole = roleRepository.insert(new UserRole(roleName));
        mongoTemplate.update(AppUser.class).matching(Criteria.where("email").is(email))
                .apply(new Update().push("roles").value(roleName))
                .first();
        log.info("adding new ROle {} to a user {}", roleName, email);

        AppUser user = userRepository.findByEmailIgnoreCase(email);

        user.getRoleList().add(userRole);

    }
}