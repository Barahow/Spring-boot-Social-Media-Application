package dev.social.media.security.service;

import dev.social.media.security.model.AppUser;
import dev.social.media.security.model.Post;
import dev.social.media.security.repository.PostRepository;
import dev.social.media.security.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class HashTagService {



    private final PostRepository postRepository;
    private final UserRepository userRepository;


    public Post createHashTag(String email, List<String> hashTag) {

        AppUser user = userRepository.findByEmailIgnoreCase(email);




        if (user == null) {
            throw  new UsernameNotFoundException("User not found with that email");

        }
        LocalDateTime dateTime = LocalDateTime.now();


        Post post = new Post();

        post.setHashtags(hashTag);




        return  postRepository.insert(post);



    }

}
