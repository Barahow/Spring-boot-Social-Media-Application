package dev.social.media.security.controller;


import dev.social.media.security.model.*;
import dev.social.media.security.service.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/api/v1/timeline")
public class UserTimeLineController {

    private final UserTimeLineService userTimeLineService;


    private final UserService userService;

    private final PostService postService;

    private final CommentService commentService;
    private final JwtTokenProvider jwtTokenProvider;

@GetMapping("/{email}")
public ResponseEntity<?> getSharedToTimeLine(@PathVariable("email") String email) {

    AppUser appUser = userService.getUser(email);

    if (appUser== null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    }
    if (!appUser.isPrivate()) {

        userTimeLineService.allSharedContentFromUser(appUser);
        return ResponseEntity.ok(appUser);


    }else {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }


}

    @PostMapping("post/{id}")
    public ResponseEntity<?> sharePostToTimeLine(@PathVariable("id") ObjectId postId, @RequestHeader("Authorization") String authorizationHeader) {
        String loggedInUserEmail = jwtTokenProvider.getEmailFromToken(authorizationHeader);

        AppUser appUser = userService.getUser(loggedInUserEmail);

        Optional<Post> postOptional = postService.getPostById(postId);


        if (appUser == null ) {
            return ResponseEntity.notFound().build();

        }

        if (postOptional.isEmpty()) {
            return ResponseEntity.notFound().build();

        }

        assert appUser != null;
        if (!appUser.getEmail().equals(loggedInUserEmail)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }



        Post post = postOptional.get();

        Post userTimeLinePost = userTimeLineService.sharePost(post,appUser);
        return new ResponseEntity<>(userTimeLinePost,HttpStatus.CREATED);


    }

    @PostMapping("comment/{id}")
    public ResponseEntity<?> shareToCommentTimeLine(@PathVariable("id") ObjectId commentId, @RequestHeader("Authorization") String authorizationHeader) {
        String loggedInUserEmail = jwtTokenProvider.getEmailFromToken(authorizationHeader);

        AppUser appUser = userService.getUser(loggedInUserEmail);

        Optional<Comment> commentOptional = commentService.getCommentById(commentId);


        if (appUser == null ) {
            return ResponseEntity.notFound().build();

        }

        if (commentOptional.isEmpty()) {
            return ResponseEntity.notFound().build();

        }


        if (!appUser.getEmail().equals(loggedInUserEmail)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }



        Comment comment =commentOptional.get();

        Comment userTimeLineComment = userTimeLineService.shareComment(comment,appUser);
        return new ResponseEntity<>(userTimeLineComment,HttpStatus.CREATED);


    }



}
