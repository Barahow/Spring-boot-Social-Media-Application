package dev.social.media.security.controller;

import dev.social.media.security.dto.PostContent;
import dev.social.media.security.model.*;
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
import java.util.ArrayList;
import java.util.Optional;
import java.util.List;
@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/api/v1/follow")
public class FollowController {


    @Autowired
    private final FollowService followService;



    private final UserService userService;

    private final JwtTokenProvider jwtTokenProvider;



    @PostMapping("/{id}")
    public ResponseEntity<?> createFollow(@PathVariable("id")  ObjectId targetUserId, @RequestHeader("Authorization") String authorizationHeader) {
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

            if (!targetFollow.isPrivate()) {
                Follow follow = followService.createFollow(appUser, targetFollow);
                userService.updateUser(appUser);
                return new ResponseEntity<>(follow, HttpStatus.CREATED);
            }else {
                FollowRequest followRequest = followService.acceptFollowRequest(appUser,targetFollow);
                if (followRequest!= null) {
                    return new ResponseEntity<>(followRequest,HttpStatus.CREATED);

                }else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            }


        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @PostMapping("/accept/{requestId}")
    public ResponseEntity<FollowRequest> acceptFollowRequest(@PathVariable("requestId") ObjectId requestId, @RequestHeader("Authorization") String authorizationHeader) {
        Optional<FollowRequest> followRequestOptional = followService.getFollowRequestById(requestId);
        String loggedInUserEmail = jwtTokenProvider.getEmailFromToken(authorizationHeader);

        AppUser appUser = userService.getUser(loggedInUserEmail);



        if (followRequestOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (!appUser.getEmail().equals(loggedInUserEmail)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }



        FollowRequest followRequest = followRequestOptional.get();

        if (appUser.isPrivate()) {

            // Process the acceptance of the follow request
            FollowRequest follow = followService.acceptFollowRequest(followRequest.getSender(),followRequest.getReceiver());

            if (follow != null) {
                return new ResponseEntity<>(follow, HttpStatus.CREATED);

            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

       return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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

    @GetMapping("/followers/{id}")
    public ResponseEntity<List<AppUser>> getNumberOfFollowers(@PathVariable("id") ObjectId id) {
        Optional<AppUser> appUserOptional = userService.findById(id);

        if (appUserOptional.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        }

        AppUser following = appUserOptional.get();




        List<AppUser> followers = followService.getAllFollowers(following);

        return ResponseEntity.ok(followers);
    }



    @GetMapping("/following/{id}")
    public ResponseEntity<List<AppUser>> getNumberOfFollowing(@PathVariable("id") ObjectId id) {
        Optional<AppUser> appUserOptional = userService.findById(id);

        if (appUserOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        }

        AppUser appUser = appUserOptional.get();




        List<AppUser> followings =followService.findAllFollowings(appUser);

        return ResponseEntity.ok(followings);
    }
}



