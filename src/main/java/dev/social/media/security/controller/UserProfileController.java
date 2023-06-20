package dev.social.media.security.controller;

import dev.social.media.security.model.AppUser;
import dev.social.media.security.service.JwtTokenProvider;
import dev.social.media.security.service.UserProfileService;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparkproject.jetty.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/api/v1/profile")
public class UserProfileController {

    private UserProfileService userProfileService;

    private final JwtTokenProvider jwtTokenProvider;




    @GetMapping("/user/{email}")
    public ResponseEntity<AppUser> getUserAccount(@PathVariable @NotBlank String email, @RequestHeader("Authorization") String authorizationHeader) {


        String loggedInUserEmail = jwtTokenProvider.getEmailFromToken(authorizationHeader);


        AppUser appUser = userProfileService.getUser(email);



        if (!appUser.getEmail().equals(loggedInUserEmail)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED_401).build();
        }



        return new ResponseEntity<AppUser>(userProfileService.getUser(email), HttpStatusCode.valueOf(HttpStatus.OK_200));


    }


    @PutMapping("/user/{email}")
    public ResponseEntity<AppUser> editAccount(@PathVariable("email") String email,@RequestBody AppUser updateUser, @RequestHeader("Authorization") String authorizationHeader) {
        // retrieve the logged in user email

        String loggedInUserEmail = jwtTokenProvider.getEmailFromToken(authorizationHeader);


        AppUser appUser = userProfileService.getUser(email);

        if (appUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND_404).build();
        }

        if (!appUser.getEmail().equals(loggedInUserEmail)) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED_401).build();
        }

        // update the updated at each time update the specific user
        LocalDateTime dateTime = LocalDateTime.now();
        // update the user from content
        appUser.setEmail(updateUser.getEmail());
        appUser.setBio(updateUser.getBio());
        appUser.setUserName(updateUser.getUserName());
        appUser.setAddress(updateUser.getAddress());
        appUser.setPassword(updateUser.getPassword());
        appUser.setProfilePicture(updateUser.getProfilePicture());
        appUser.setFirstName(appUser.getFirstName());
        appUser.setLastName(appUser.getLastName());
        appUser.setUpdatedAt(dateTime);

        return ResponseEntity.ok(userProfileService.updateUser(appUser));

    }



    @DeleteMapping("/user/{email}")
    public ResponseEntity<AppUser> deleteAccount(@PathVariable("email") String email, @RequestHeader("Authorization") String authorizationHeader) {
        String loggedInUserEmail = jwtTokenProvider.getEmailFromToken(authorizationHeader);

        AppUser user = userProfileService.getUser(email);

        if (user == null) {
            return ResponseEntity.notFound().build();

        }

        if (!user.getEmail().equals(loggedInUserEmail)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED_401).build();
        }

        userProfileService.deleteAccount(user);
        // delete the account
        return ResponseEntity.ok(user);
    }






}
