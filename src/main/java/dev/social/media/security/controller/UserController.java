package dev.social.media.security.controller;

import dev.social.media.security.model.AppUser;
import dev.social.media.security.service.JwtTokenProvider;
import dev.social.media.security.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.POST;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.server.Uri;
import org.sparkproject.jetty.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/user")
    public ResponseEntity<Page<AppUser>> getAllUsers(@RequestParam(defaultValue = "0") @Min(0) int page,
                                                     @RequestParam(defaultValue = "10") @Max(10) int size) {

        Page<AppUser> users = userService.getAllUsers(PageRequest.of(page,size));

        return ResponseEntity.ok().body(users);
    }

    @PostMapping("/user/save")
    public ResponseEntity<AppUser> createUser(@RequestBody @Valid AppUser user) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/user/save").toUriString());
        AppUser createUser = userService.createUser(user);

        return ResponseEntity.created(uri).body(createUser);

    }


    @GetMapping("/user/{email}")
    public ResponseEntity<AppUser> getSingleUser(@PathVariable @NotBlank String email) {
        return new ResponseEntity<AppUser>(userService.getUser(email), HttpStatusCode.valueOf(HttpStatus.OK_200));


    }


    // edit a user

    @PutMapping("/user/{email}")
    public ResponseEntity<AppUser> editAccount(@PathVariable("email") String email,@RequestBody AppUser updateUser, @RequestHeader("Authorization") String authorizationHeader) {
        // retrieve the logged in user email

        String loggedInUserEmail = jwtTokenProvider.getEmailFromToken(authorizationHeader);


        AppUser appUser = userService.getUser(loggedInUserEmail);

        if (appUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED_401).build();
        }

        if (!appUser.getEmail().equals(loggedInUserEmail)) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED_401).build();
        }

        // update the updated at each time update the specific user
        LocalDateTime dateTime = LocalDateTime.now();
        // update the user from content
        appUser.setEmail(updateUser.getEmail());
        appUser.setUserName(updateUser.getUserName());
        appUser.setAddress(updateUser.getAddress());
        appUser.setPassword(updateUser.getPassword());
        appUser.setProfilePicture(updateUser.getProfilePicture());
        appUser.setFirstName(appUser.getFirstName());
        appUser.setLastName(appUser.getLastName());
        appUser.setUpdatedAt(dateTime);

        return ResponseEntity.ok(userService.updateUser(appUser));

    }



    @DeleteMapping("/user/{email}")
    public ResponseEntity<AppUser> deleteAccount(@PathVariable("email") String email, @RequestHeader("Authorization") String authorizationHeader) {
        String loggedInUserEmail = jwtTokenProvider.getEmailFromToken(authorizationHeader);

        AppUser user = userService.getUser(loggedInUserEmail);

        if (user == null) {
            return ResponseEntity.notFound().build();

        }

        if (!user.getEmail().equals(loggedInUserEmail)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED_401).build();
        }

        userService.deleteUser(user);
        // delete the account
        return ResponseEntity.ok(user);
    }







}
