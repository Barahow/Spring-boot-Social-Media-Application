package dev.social.media.security.service;

import dev.social.media.security.model.AppUser;
import dev.social.media.security.model.Comment;
import dev.social.media.security.model.Like;
import dev.social.media.security.model.Post;
import dev.social.media.security.repository.CommentRepository;
import dev.social.media.security.repository.LikeRepository;
import dev.social.media.security.repository.PostRepository;
import dev.social.media.security.repository.UserRepository;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
@Service
@AllArgsConstructor
@Slf4j
public class LikeService {
/*    @Id
    private ObjectId id;

    private AppUser user;

    private Post post;

    private Comment comment;

    private LocalDateTime createdAt;*/





    @Autowired
    private final LikeRepository likeRepository;

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    private MongoTemplate mongoTemplate;
    public Like LikePost(String email, ObjectId postId) {

        AppUser user = userRepository.findByEmailIgnoreCase(email);

        Optional<Post> postOptional = postRepository.findById(postId);



        if (postOptional.isEmpty()) {
            throw new NullPointerException("post not found");
        }

        Post post = postOptional.get();

        if (user == null) {
            throw  new UsernameNotFoundException("User not found with that email");

        }
        LocalDateTime dateTime = LocalDateTime.now();





        return likeRepository.insert(new Like(new ObjectId(),user,post,null,dateTime));



    }
    public Like LikeComment(String email, ObjectId commentId) {

        AppUser user = userRepository.findByEmailIgnoreCase(email);
        Optional<Comment> commentOptional = commentRepository.findById(commentId);


        if (commentOptional.isEmpty()) {
            throw new NullPointerException("There doesnt seem to be a comment to like");
        }

        Comment comment = commentOptional.get();

        if (user == null) {
            throw  new UsernameNotFoundException("User not found with that email");

        }
        LocalDateTime dateTime = LocalDateTime.now();






        return likeRepository.insert(new Like(new ObjectId(),user,null,comment,dateTime));



    }

    public Optional<Like> getLike(ObjectId likeId) {
        return likeRepository.findById(likeId);

    }


    public void removeLike(Like like) {
        likeRepository.delete(like);
    }

    // get post by a specific user

    public Optional<Like> getLikeFromUser(AppUser user) {
        return likeRepository.findByUser(user);
    }



    public Like updatePost(Like existingLike) {
        return likeRepository.save(existingLike);
    }

}
