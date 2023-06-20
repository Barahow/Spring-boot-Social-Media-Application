package dev.social.media.security.service;

import dev.social.media.security.model.AppUser;
import dev.social.media.security.repository.UserProfileRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@AllArgsConstructor
@Slf4j
public class UserProfileService {


    private UserProfileRepository userProfileRepository;





    public AppUser getUser(String email) {
        // santize input by checking if the input is null or empty

        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("email cannnot be null");
        }
        log.info("fetching email {}", email);
        return userProfileRepository.findByEmailIgnoreCase(email);
    }





    public AppUser updateUser(AppUser user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        log.info("updating new user to the database {}", user.getEmail());

        return userProfileRepository.save(user);
    }

    public void deleteAccount(AppUser appUser) {

        if (appUser == null) {
            throw new NullPointerException("User does not seem exist");
        }

        log.info("You are deleting this user {}", appUser.getUserName());
        userProfileRepository.delete(appUser);

    }

    public Optional<AppUser> findById(ObjectId userId) {
        return userProfileRepository.findById(userId);
    }


}

