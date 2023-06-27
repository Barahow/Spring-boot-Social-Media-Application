package dev.social.media.security.service;


import dev.social.media.security.model.AppUser;
import dev.social.media.security.model.Comment;
import dev.social.media.security.model.Post;
import dev.social.media.security.repository.CommentRepository;
import dev.social.media.security.repository.PostRepository;
import dev.social.media.security.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class SearchService {



    @Autowired
    private final CommentRepository commentRepository;

    private final PostRepository postRepository;
    private final UserRepository userRepository;



    // this method will return all username that share the start of the String
    // It uses regex to look in the databse
    // ^ indicates the start of the string.
    //" + userName + " represents the provided userName.
    //\\d* matches any number of digits (0 or more).
    //$ indicates the end of the string.
    //The "i" flag is used to perform a case-insensitive search.

    // so if a write mike, i will get mike2, mike2, and mik4 etc.
    public List<AppUser> getUsersByUserName(String userName) {
        if (userName == null || userName.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        log.info("Searching for usernames containing: {}", userName);

        return userRepository.findByUserNameRegex("^" + userName + "\\d*$", "i");
    }


    public List<Comment> searchCommentByContent(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            throw new IllegalArgumentException("Keyword cannot be null or empty");
        }

        log.info("Searching for comments with keyword: {}", keyword);
        return commentRepository.findByContentContainingIgnoreCase(keyword);
    }

    public List<Post> searchPostByContent(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            throw new IllegalArgumentException("Keyword cannot be null or empty");
        }

        log.info("Searching for comments with keyword: {}", keyword);
        return postRepository.findByContentContainingIgnoreCase(keyword);
    }


    public List<Post> getPostByUserAndContent(String userName, String content) {
        if (userName == null || userName.isEmpty() || content == null || content.isEmpty()) {
            throw new IllegalArgumentException("Username and content cannot be null or empty");
        }

        log.info("Searching for post with username: {} and content: {}", userName, content);
        return postRepository.findByUserUserNameAndContent(userName, content);
    }

    public List<Comment> getCommentsByUserAndContent(String userName, String content) {
        if (userName == null || userName.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        log.info("Searching for comments by username: {}", userName);
        return commentRepository.findByUserUsernameAndContent(userName,content);
    }






}
