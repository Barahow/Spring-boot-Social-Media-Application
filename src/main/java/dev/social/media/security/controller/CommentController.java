package dev.social.media.security.controller;

import dev.social.media.security.dto.CommentContent;
import dev.social.media.security.dto.PostContent;
import dev.social.media.security.dto.UpdateCommentContent;
import dev.social.media.security.model.AppUser;
import dev.social.media.security.model.Comment;
import dev.social.media.security.model.Post;
import dev.social.media.security.service.CommentService;
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
import java.util.Map;
import java.util.Optional;
@Slf4j
@AllArgsConstructor
@RequestMapping("/api/v1/comment")
public class CommentController {


    @Autowired
    private final CommentService commentService;


    private final PostService postService;

    private final UserService userService;

    private final JwtTokenProvider jwtTokenProvider;



    @PostMapping("/post/{id}")
    public ResponseEntity<Comment> createComment(@PathVariable ObjectId postId, @Valid @RequestBody CommentContent commentContent, @RequestHeader("Authorization") String authorizationHeader) {
        String loggedInUserEmail = jwtTokenProvider.getEmailFromToken(authorizationHeader);

        AppUser appUser = userService.getUser(loggedInUserEmail);

        Optional<Post> postOptional = postService.getPostById(postId);

        if (appUser== null) {
            return ResponseEntity.notFound().build();
        }

        if (postOptional.isEmpty()) {
        return ResponseEntity.notFound().build();

        }
        Post post = postOptional.get();

        // this one checks whether the logged in user is the one leaving the comment
        if (!appUser.getEmail().equals(loggedInUserEmail)&& !post.getUser().getEmail().equals(loggedInUserEmail)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }



        String content= commentContent.getContent();

        Comment comment = commentService.createComment(post.getId(),content,appUser.getEmail());


        return new ResponseEntity<>(comment,HttpStatus.CREATED);

    }


    @DeleteMapping("/post/{id}")
    public ResponseEntity<Comment> deleteComment(@PathVariable("id") ObjectId commentId, @RequestHeader("Authorization") String authorizationToken) {
        String loggedInUserEmail = jwtTokenProvider.getEmailFromToken(authorizationToken);

        AppUser appUser = userService.getUser(loggedInUserEmail);

        Optional<Comment> commentOptional = commentService.getCommentById(commentId);

        if (commentOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }


        Comment comment = commentOptional.get();

        if (!comment.getUser().getEmail().equals(appUser.getEmail())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();


        }

        commentService.deleteComment(comment);

        return ResponseEntity.ok(comment);


    }


    //update a post

    @PutMapping("/post/{id}")

    public ResponseEntity<Comment> editComment(@PathVariable ObjectId commentId, @RequestHeader("Authorization") @RequestBody UpdateCommentContent updatePost, String authorizationHeader) {

        String loggedInUserEmail = jwtTokenProvider.getEmailFromToken(authorizationHeader);

        Optional<Comment> commentOptional = commentService.getCommentById(commentId);


        // makes sure the user not only is authorized but it is the same user
        // who made the post in the first place
        AppUser user= userService.getUser(loggedInUserEmail);


        if (commentOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Comment existingComment = commentOptional.get();

        if (!existingComment.getUser().getEmail().equals(user.getEmail())) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        }

        LocalDateTime dateTime = LocalDateTime.now();
        existingComment.setContent(updatePost.getContent());
        existingComment.setUpdateAt(dateTime);


        commentService.updateComment(existingComment);

        return ResponseEntity.ok(existingComment);

    }





    @GetMapping("/{id}")
    public ResponseEntity<Comment> getSinglePost(@PathVariable("id") ObjectId id) {
        Optional<Comment> commentOptional= commentService.getCommentById(id);
        if (commentOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(commentOptional.get());
    }

}


