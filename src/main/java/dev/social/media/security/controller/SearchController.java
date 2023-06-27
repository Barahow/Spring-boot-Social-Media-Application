package dev.social.media.security.controller;

import dev.social.media.security.model.AppUser;
import dev.social.media.security.model.Comment;
import dev.social.media.security.model.Post;
import dev.social.media.security.service.JwtTokenProvider;
import dev.social.media.security.service.SearchService;
import dev.social.media.security.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@AllArgsConstructor
@RequestMapping("/api/v1/search")
public class SearchController {


    private final SearchService searchService;

    private final UserService userService;

    private final JwtTokenProvider jwtTokenProvider;


    @GetMapping("/user/{username}")
    public ResponseEntity<List<AppUser>> searchUsername(@PathVariable("username")  String userName, @RequestHeader("Authorization") String authorizationHeader ) {
        String loggedInUser = jwtTokenProvider.getEmailFromToken(authorizationHeader);
        List<AppUser> appUser = searchService.getUsersByUserName(userName);






        if (appUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }






            return new ResponseEntity<>(appUser,HttpStatus.CREATED);
        
    }


    @GetMapping("/comment/{keyword}")
    public ResponseEntity<List<Comment>> searchComments(@PathVariable("keyword")  String keyWords) {

        List<Comment> commentList = searchService.searchCommentByContent(keyWords);







        if (commentList.isEmpty()) {
            log.error("no comments of that kind found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        }





            return new ResponseEntity<>(commentList,HttpStatus.CREATED);


    }


    @GetMapping("/post/{keyword}")
    public ResponseEntity<List<Post>> searchPost(@PathVariable("keyword")  String keyWords) {

        List<Post> postList = searchService.searchPostByContent(keyWords);







        if (postList.isEmpty()) {
            log.error("no comments of that kind found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        }





        return new ResponseEntity<>(postList,HttpStatus.CREATED);


    }


    @GetMapping("/post/{username}/{content}")
    public ResponseEntity<List<Post>> searchUserPost(@PathVariable("username") String userName,@PathVariable("content")  String content) {



        List<Post> postList = searchService.getPostByUserAndContent(userName,content);







        if (postList.isEmpty()) {
            log.error("no comments of that kind found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        }





        return new ResponseEntity<>(postList,HttpStatus.CREATED);


    }


    @GetMapping("/comment/{username}/{content}")
    public ResponseEntity<List<Comment>> searchUserComment(@PathVariable("username") String userName,@PathVariable("content")  String content) {



        List<Comment> commentList = searchService.getCommentsByUserAndContent(userName,content);







        if (commentList.isEmpty()) {
            log.error("no comments of that kind found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        }





        return new ResponseEntity<>(commentList,HttpStatus.CREATED);


    }



}


