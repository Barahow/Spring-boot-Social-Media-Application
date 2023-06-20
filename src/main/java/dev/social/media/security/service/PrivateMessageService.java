package dev.social.media.security.service;

import dev.social.media.security.model.*;
import dev.social.media.security.repository.PostRepository;
import dev.social.media.security.repository.PrivateMessageRepository;
import dev.social.media.security.repository.PrivateMessageRequestRepository;
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

        private final PrivateMessageRequestRepository privateMessageRequestRepository;
        private final PostRepository postRepository;
        private final UserRepository userRepository;
        private final SimpMessagingTemplate messagingTemplate;




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




        public void deleteMessageRequest(PrivateMessageRequest privateMessageRequest) {
            privateMessageRequestRepository.delete(privateMessageRequest);
        }


    public void deleteMessage(PrivateMessage privateMessage) {
        privateMessageRepository.delete(privateMessage);
    }

    public Optional<PrivateMessage> getPrivateMessageId(ObjectId privateMessage) {
        return privateMessageRepository.findById(privateMessage);

    }

    public Optional<PrivateMessageRequest> getPrivateMessageRequestId(ObjectId privateMessageRequest) {
        return privateMessageRequestRepository.findById(privateMessageRequest);

    }




    public void savePrivateMessage(PrivateMessage privateMessage) {
    privateMessageRepository.save(privateMessage);
    }

    public void savePrivateMessageRequest(PrivateMessageRequest privateMessageRequest) {
        privateMessageRequestRepository.save(privateMessageRequest);
    }





    public PrivateMessageRequest acceptFollowRequest(AppUser sender, AppUser receiver, String messageContent) {

        AppUser sender1 = userRepository.findByEmailIgnoreCase(sender.getEmail());
        AppUser receiver1 = userRepository.findByEmailIgnoreCase(receiver.getEmail());

        if (sender1 == null && receiver1== null) {
            throw new NullPointerException("Both the sender and the reciever are null");
        }

        if (sender1 != null && receiver1!= null) {

            LocalDateTime dateTime = LocalDateTime.now();

            PrivateMessageRequest privateMessageRequest = new PrivateMessageRequest(new ObjectId(),messageContent,sender1,receiver1,dateTime);

            messagingTemplate.convertAndSend("/topic/messages",privateMessageRequest);;
            privateMessageRequestRepository.insert(privateMessageRequest);

            return privateMessageRequest;

        }else {
            throw new IllegalStateException("there might not be a follow request to check");
        }
    }

}


