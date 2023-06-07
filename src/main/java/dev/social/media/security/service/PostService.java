package dev.social.media.security.service;

import dev.social.media.security.model.AppUser;
import dev.social.media.security.model.Post;
import dev.social.media.security.repository.PostRepository;
import dev.social.media.security.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class PostService {

    @Autowired
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    private MongoTemplate mongoTemplate;
    public Post createPost(String email, String postContent) {

        AppUser user = userRepository.findByEmailIgnoreCase(email);




        if (user == null) {
            throw  new UsernameNotFoundException("User not found with that email");

        }
        LocalDateTime dateTime = LocalDateTime.now();





        return postRepository.insert(new Post(new ObjectId(),postContent,dateTime,null,user));



    }


    public Optional<Post> getPostById(ObjectId postId) {
        return postRepository.findById(postId);

    }


    public void deletePost(Post post) {
        postRepository.delete(post);
    }

    // get post by a specific user

    public Optional<Post >getPostFromUser(AppUser user) {
        return postRepository.findByUser(user);
    }



    public Post updatePost(Post existingPost) {
        return postRepository.save(existingPost);
    }

}
