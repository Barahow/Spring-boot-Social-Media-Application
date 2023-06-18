package dev.social.media.security.service;

import dev.social.media.security.model.AppUser;
import dev.social.media.security.model.Post;
import dev.social.media.security.model.PrivateMessage;
import dev.social.media.security.repository.PostRepository;
import dev.social.media.security.repository.PrivateMessageRepository;
import dev.social.media.security.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import  java.util.List;
@Service
@AllArgsConstructor
@Slf4j
public class PrivateMessageService {


        @Autowired
        private final PrivateMessageRepository privateMessageRepository;
        private final PostRepository postRepository;
        private final UserRepository userRepository;

    private final SimpMessagingTemplate messagingTemplate;

        @Autowired
        private MongoTemplate mongoTemplate;




    public PrivateMessage createPrivateMessage(String senderEmail, String recipientEmail, String messageContent) {
        AppUser sender = userRepository.findByEmailIgnoreCase(senderEmail);
        AppUser recipient = userRepository.findByEmailIgnoreCase(recipientEmail);
        LocalDateTime dateTime = LocalDateTime.now();

        if (sender== null && recipient== null) {
            throw new NullPointerException("no such user or recipient");
        }


        PrivateMessage privateMessage = new PrivateMessage(new ObjectId(),messageContent,new ArrayList<>(), new ArrayList<>(),dateTime,sender,recipient);

        messagingTemplate.convertAndSend("/topic/messages",privateMessage);;
        privateMessageRepository.insert(privateMessage);




        return privateMessage;
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

    public void savePrivateMessage(PrivateMessage privateMessage) {
    privateMessageRepository.save(privateMessage);
    }
}
