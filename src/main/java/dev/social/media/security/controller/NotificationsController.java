package dev.social.media.security.controller;

import dev.social.media.security.dto.NotificationContent;
import dev.social.media.security.dto.PrivateMessageContent;
import dev.social.media.security.model.*;
import dev.social.media.security.repository.UserFollowRepository;
import dev.social.media.security.service.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Transient;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Slf4j
@AllArgsConstructor
@Transient
public class NotificationsController {

    private final UserService userService;

    private final FollowService followService;
    private final NotificationService notificationService;
    private final JwtTokenProvider jwtTokenProvider;

    private SimpMessagingTemplate messagingTemplate;

    @PostMapping("/send/{id}")
    public ResponseEntity<?> sendPrivateMessage(@PathVariable("id") ObjectId targetUserId, @RequestBody @Valid NotificationContent notificationContent, @RequestHeader("Authorization") String authorizationHeader) {
        String loggedInUserEmail = jwtTokenProvider.getEmailFromToken(authorizationHeader);

        AppUser appUser = userService.getUser(loggedInUserEmail);
        Optional<AppUser> targetUserOptional = userService.findById(targetUserId);

        String notificationType = notificationContent.getNotificationType();
        String content = notificationContent.getContent();

        if (appUser == null && targetUserOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }


        assert appUser != null;
        if (!appUser.getEmail().equals(loggedInUserEmail)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }



        if (targetUserOptional.isPresent()) {


            AppUser targetUser = targetUserOptional.get();

            //  //TODO  handles different types of notifications such as new post from user you follow, like, comment etc
            if (followService.isFollowed(appUser,targetUser) && notificationType.equals("post")) {
                // new post
                Notifications notifications = notificationService.createNotifications(appUser.getEmail(), targetUser.getEmail(), content);

                // send the private message to target user

                notifications.setRead(true);
                messagingTemplate.convertAndSendToUser(targetUser.getEmail(), "/topic/notifications", notifications);

                // save the private message in the database
                userService.updateUser(appUser);


                return new ResponseEntity<>(notifications, HttpStatus.CREATED);


            }
            if (followService.isFollowed(appUser,targetUser) && notificationType.equals("like")) {
                // new post
                Notifications notifications = notificationService.createNotifications(appUser.getEmail(), targetUser.getEmail(), content);

                // send the private message to target user

                notifications.setRead(true);
                messagingTemplate.convertAndSendToUser(targetUser.getEmail(), "/topic/notifications", notifications);

                // save the private message in the database
                userService.updateUser(appUser);


                return new ResponseEntity<>(notifications, HttpStatus.CREATED);


            }
            if (followService.isFollowed(appUser,targetUser) && notificationType.equals("message")) {
                // new post
                Notifications notifications = notificationService.createNotifications(appUser.getEmail(), targetUser.getEmail(), content);

                // send the private message to target user

                notifications.setRead(true);
                messagingTemplate.convertAndSendToUser(targetUser.getEmail(), "/topic/notifications", notifications);

                // save the private message in the database
                userService.updateUser(appUser);


                return new ResponseEntity<>(notifications, HttpStatus.CREATED);


            }

            if (followService.isFollowed(appUser,targetUser) && notificationType.equals("comment")) {
                // new comment
                Notifications notifications = notificationService.createNotifications(appUser.getEmail(), targetUser.getEmail(), content);

                // send the private message to target user

                notifications.setRead(true);
                messagingTemplate.convertAndSendToUser(targetUser.getEmail(), "/topic/notifications", notifications);

                // save the private message in the database
                userService.updateUser(appUser);


                return new ResponseEntity<>(notifications, HttpStatus.CREATED);


            }


            else if (notificationType.equals("follow request")) {
                Notifications notifications = notificationService.createNotifications(appUser.getEmail(), targetUser.getEmail(), "follow request from "+appUser.getEmail());

                // send the private message to target user

                notifications.setRead(true);
                messagingTemplate.convertAndSendToUser(targetUser.getEmail(), "/topic/notifications", notifications);

                // save the private message in the database
                userService.updateUser(appUser);


                return new ResponseEntity<>(notifications, HttpStatus.CREATED);


            }

        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

}
