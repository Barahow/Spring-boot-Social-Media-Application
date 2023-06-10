package dev.social.media.security.controller;

import dev.social.media.security.dto.PostContent;
import dev.social.media.security.model.AppUser;
import dev.social.media.security.model.Post;
import dev.social.media.security.service.JwtTokenProvider;
import dev.social.media.security.service.PostService;
import dev.social.media.security.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.shaded.org.apache.http.client.AuthCache;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@Slf4j
@AllArgsConstructor

@RequestMapping("/api/v1/post")

public class PostController {


    @Autowired
    private final PostService postService;

    private final UserService userService;

    private final JwtTokenProvider jwtTokenProvider;



    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody @Valid PostContent postContent, @RequestHeader("Authorization") String authorizationHeader) {
        String loggedInUserEmail = jwtTokenProvider.getEmailFromToken(authorizationHeader);

        AppUser appUser = userService.getUser(loggedInUserEmail);

        if (appUser== null) {
            return ResponseEntity.notFound().build();
        }

        if (!appUser.getEmail().equals(loggedInUserEmail)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }


        String content = postContent.getContent();

        Post post = postService.createPost(appUser.getEmail(),content);


        return new ResponseEntity<>(post,HttpStatus.CREATED);

    }


    @DeleteMapping("/post/{id}")
    public ResponseEntity<Post> deletePost(@PathVariable("id") ObjectId postId, @RequestHeader("Authorization") String authorizationToken) {
        String loggedInUserEmail = jwtTokenProvider.getEmailFromToken(authorizationToken);

        AppUser appUser = userService.getUser(loggedInUserEmail);

        Optional<Post> postOptional = postService.getPostById(postId);

        if (postOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }


        Post post = postOptional.get();

        if (!post.getUser().getEmail().equals(appUser.getEmail())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();


        }

        postService.deletePost(post);

        return ResponseEntity.ok(post);


    }


    //update a post

    @PutMapping("/post/{id}")

    public ResponseEntity<Post> editPost(@PathVariable ObjectId postId, @RequestHeader("Authorization") @RequestBody PostContent updatePost, String authorizationHeader) {

        String loggedInUserEmail = jwtTokenProvider.getEmailFromToken(authorizationHeader);

        Optional<Post> postOptional = postService.getPostById(postId);


        // makes sure the user not only is authorized but it is the same user
        // who made the post in the first place
       AppUser user= userService.getUser(loggedInUserEmail);


        if (postOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Post existingPost = postOptional.get();

        if (!existingPost.getUser().getEmail().equals(user.getEmail())) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        }

        LocalDateTime dateTime = LocalDateTime.now();
        existingPost.setContent(updatePost.getContent());
        existingPost.setUpdatedAt(dateTime);


        postService.updatePost(existingPost);

        return ResponseEntity.ok(existingPost);

    }




   // TODO "Decide whether everyone can see a user post or whether it should be specific to who
   //  a user follows"
    @GetMapping("/{id}")
    public ResponseEntity<Post> getSinglePost(@PathVariable("id") ObjectId id) {
        Optional<Post> post = postService.getPostById(id);
        if (post.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(post.get());
    }

}

