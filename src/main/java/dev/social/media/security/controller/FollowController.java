package dev.social.media.security.controller;

import dev.social.media.security.dto.PostContent;
import dev.social.media.security.model.AppUser;
import dev.social.media.security.model.Follow;
import dev.social.media.security.model.Post;
import dev.social.media.security.service.FollowService;
import dev.social.media.security.service.JwtTokenProvider;
import dev.social.media.security.service.PostService;
import dev.social.media.security.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@RequestMapping("/api/v1/follow")
public class FollowController {


    @Autowired
    private final FollowService followService;



    private final UserService userService;

    private final JwtTokenProvider jwtTokenProvider;



    @PostMapping("/{id}")
    public ResponseEntity<Follow> createFollow(@PathVariable("id")  ObjectId targetUserId, @RequestHeader("Authorization") String authorizationHeader) {
        String loggedInUserEmail = jwtTokenProvider.getEmailFromToken(authorizationHeader);

        AppUser appUser = userService.getUser(loggedInUserEmail);
        Optional<AppUser> targetUser = userService.findById(targetUserId);

        if (appUser == null && targetUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        assert appUser != null;
        if (!appUser.getEmail().equals(loggedInUserEmail)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (targetUser.isPresent()) {


            AppUser targetFollow = targetUser.get();

            Follow follow = followService.createFollow(appUser, targetFollow);


            return new ResponseEntity<>(follow, HttpStatus.CREATED);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
        @DeleteMapping("/follow/{id}")
        public ResponseEntity<Follow> deleteFollow (@PathVariable("id") ObjectId
        targetUserId, @RequestHeader("Authorization") String authorizationToken){
            String loggedInUserEmail = jwtTokenProvider.getEmailFromToken(authorizationToken);

            AppUser loggedInUser = userService.getUser(loggedInUserEmail);
            Optional<AppUser> targetUserOptional = userService.findById(targetUserId);

            if (loggedInUser == null || targetUserOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            AppUser targetUser = targetUserOptional.get();

            boolean isFollowed = followService.isFollowed(loggedInUser, targetUser);

            if (!isFollowed || loggedInUser.getFollowings().stream().noneMatch(follow -> follow.getFollowedUser().equals(targetUser))) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            followService.removeFollow(loggedInUser, targetUser);

            return ResponseEntity.ok().build();
        }

    }


