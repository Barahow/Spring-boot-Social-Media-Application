package dev.social.media.security.controller;

import dev.social.media.security.model.AppUser;
import dev.social.media.security.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.POST;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.server.Uri;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


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






}
