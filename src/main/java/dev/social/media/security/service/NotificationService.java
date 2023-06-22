package dev.social.media.security.service;


import dev.social.media.security.model.AppUser;
import dev.social.media.security.model.Notifications;
import dev.social.media.security.model.PrivateMessage;
import dev.social.media.security.repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service

@AllArgsConstructor
@Slf4j
public class NotificationService {
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;




    public Notifications createNotifications(String senderEmail, String recipientEmail, String notificationMessage) {
        AppUser sender = userRepository.findByEmailIgnoreCase(senderEmail);
        AppUser recipient = userRepository.findByEmailIgnoreCase(recipientEmail);
        LocalDateTime dateTime = LocalDateTime.now();

        if (sender== null && recipient== null) {
            throw new NullPointerException("no such user or recipient");
        }


        Notifications notifications = new Notifications(new ObjectId(),notificationMessage,sender,recipient,dateTime,false);



        messagingTemplate.convertAndSend("/topic/notifications",notifications);;





        return notifications;
    }



}
