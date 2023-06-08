package dev.social.media.security.controller;

import dev.social.media.security.model.AppUser;
import dev.social.media.security.model.Comment;
import dev.social.media.security.model.Like;
import dev.social.media.security.model.Post;
import dev.social.media.security.service.JwtTokenProvider;
import dev.social.media.security.service.LikeService;
import dev.social.media.security.service.PostService;
import dev.social.media.security.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
@RestController
@Slf4j
@AllArgsConstructor

@RequestMapping("/api/v1/like")

public class LikeController {


    @Autowired
    private LikeService likeService;

    private final PostService postService;

    private final UserService userService;

    private final JwtTokenProvider jwtTokenProvider;



    @PostMapping("/post/{id}")
    public ResponseEntity<Like> likePost(@PathVariable  ObjectId postId, @RequestHeader("Authorization") String authorizationHeader) {
        String loggedInUserEmail = jwtTokenProvider.getEmailFromToken(authorizationHeader);

        Optional<Post> postOptional = postService.getPostById(postId);


        AppUser appUser = userService.getUser(loggedInUserEmail);

        if (appUser== null) {
            return ResponseEntity.notFound().build();
        }


        if (postOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }


        Post post = postOptional.get();

        if (!post.getUser().getEmail().equals(loggedInUserEmail)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }


        Like like = likeService.LikePost(appUser.getEmail(),post.getId());


        return new ResponseEntity<>(like,HttpStatus.CREATED);

    }



    @DeleteMapping("/like/{id}")
    public ResponseEntity<Like> removeLike(@PathVariable("id") ObjectId likeId, @RequestHeader("Authorization") String authorizationToken) {
        String loggedInUserEmail = jwtTokenProvider.getEmailFromToken(authorizationToken);

        AppUser appUser = userService.getUser(loggedInUserEmail);

        Optional<Like> likeOptional= likeService.getLike(likeId);

        if (likeOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }


        Like like = likeOptional.get();

        if (!like.getUser().getEmail().equals(appUser.getEmail())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();


        }

        likeService.removeLike(like);

        return ResponseEntity.ok(like);


    }


    //update a post




    @GetMapping("/{id}")
    public ResponseEntity<Like> getSinglePost(@PathVariable("id") ObjectId id) {
        Optional<Like> like = likeService.getLike(id);
        if (like.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(like.get());
    }

}

