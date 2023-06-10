package dev.social.media.security.service;

import dev.social.media.security.model.AppUser;
import dev.social.media.security.model.Comment;
import dev.social.media.security.model.Like;
import dev.social.media.security.model.Post;
import dev.social.media.security.repository.CommentRepository;
import dev.social.media.security.repository.LikeRepository;
import dev.social.media.security.repository.PostRepository;
import dev.social.media.security.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class CommentService {


    @Autowired
    private final CommentRepository commentRepository;

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Comment createComment(ObjectId postId, String postComment, String email) {

        AppUser user = userRepository.findByEmailIgnoreCase(email);
        Optional<Post> postOptional = postRepository.findById(postId);

        if (postId == null) {

        }


        if (user == null) {
            throw new UsernameNotFoundException("User not found with that email");

        }

        if (postOptional.isEmpty()) {
            throw new NullPointerException("no such post id exists");
        }

        Post post = postOptional.get();

        LocalDateTime dateTime = LocalDateTime.now();


        return commentRepository.insert(new Comment(new ObjectId(), postComment, dateTime, null, user, post));


    }


    public Optional<Comment> getCommentById(ObjectId commentId) {
        return commentRepository.findById(commentId);

    }


    public void deleteComment(Comment comment) {
        commentRepository.delete(comment);
    }

    // get post by a specific user

    public Optional<Comment> getCommentFromUser(AppUser user) {
        return commentRepository.findByUser(user);
    }


    public Comment updateComment(Comment existingComment) {
        return commentRepository.save(existingComment);
    }
}

