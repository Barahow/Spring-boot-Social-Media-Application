package dev.social.media.security.controller;


import dev.social.media.security.model.Post;
import dev.social.media.security.model.PrivateMessageRequest;
import dev.social.media.security.dto.PrivateMessageContent;
import dev.social.media.security.model.AppUser;
import dev.social.media.security.model.PrivateMessage;
import dev.social.media.security.service.JwtTokenProvider;
import dev.social.media.security.service.PrivateMessageService;
import dev.social.media.security.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@AllArgsConstructor
@RequestMapping("/api/v1/message")
public class PrivateMessageController {

    private final UserService userService;
    private final PrivateMessageService privateMessageService;
    private final JwtTokenProvider jwtTokenProvider;

    private SimpMessagingTemplate messagingTemplate;

    @PostMapping("/send/{id}")
    public ResponseEntity<?> sendPrivateMessage(@PathVariable("id") ObjectId targetUserId, @RequestBody @Valid PrivateMessageContent privateMessageContent, @RequestHeader("Authorization") String authorizationHeader) {
        String loggedInUserEmail = jwtTokenProvider.getEmailFromToken(authorizationHeader);

        AppUser appUser = userService.getUser(loggedInUserEmail);
        Optional<AppUser> targetUserOptional = userService.findById(targetUserId);
        String content = privateMessageContent.getContent();

        if (appUser == null && targetUserOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }


        assert appUser != null;
        if (!appUser.getEmail().equals(loggedInUserEmail)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (targetUserOptional.isPresent()) {


            AppUser targetUser = targetUserOptional.get();

            if (!targetUser.isPrivate()) {
                PrivateMessage privateMessage = privateMessageService.createPrivateMessage(appUser.getEmail(), targetUser.getEmail(), content);

                // send the private message to target user
                messagingTemplate.convertAndSendToUser(targetUser.getEmail(), "/topic/messages", privateMessage);

                // save the private message in the database
                privateMessageService.savePrivateMessage(privateMessage);
                userService.updateUser(appUser);


                return new ResponseEntity<>(privateMessage, HttpStatus.CREATED);


            } else {

                //TODO write model class for if a user is private and doesnt follow the user that sent a message
                PrivateMessageRequest privateMessageRequest = privateMessageService.acceptFollowRequest(appUser, targetUser, content);
                if (privateMessageRequest != null) {

                    // send the private message to target user
                    messagingTemplate.convertAndSendToUser(targetUser.getEmail(), "/topic/messages", privateMessageRequest);

                    // save the private message in the database
                    privateMessageService.savePrivateMessageRequest(privateMessageRequest);
                    userService.updateUser(appUser);
                    return new ResponseEntity<>(privateMessageRequest, HttpStatus.CREATED);


                }


            }

        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();    }


    @DeleteMapping("/message/{id}")
    public ResponseEntity<PrivateMessage> deletePrivateMessage(@PathVariable("id") ObjectId messageId, @RequestHeader("Authorization") String authorizationToken) {
        String loggedInUserEmail = jwtTokenProvider.getEmailFromToken(authorizationToken);

        AppUser appUser = userService.getUser(loggedInUserEmail);

        Optional<PrivateMessage> privateMessageOptional = privateMessageService.getPrivateMessageId(messageId);

        if (privateMessageOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }


        PrivateMessage privateMessage = privateMessageOptional.get();

        if (!privateMessage.getReceiver().getEmail().equals(appUser.getEmail())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();


        }

        privateMessageService.deleteMessage(privateMessage);

        return ResponseEntity.ok(privateMessage);


    }



    @DeleteMapping("/message/request/{id}")
    public ResponseEntity<PrivateMessageRequest> deletePrivateMessageRequest(@PathVariable("id") ObjectId messageId, @RequestHeader("Authorization") String authorizationToken) {
        String loggedInUserEmail = jwtTokenProvider.getEmailFromToken(authorizationToken);

        AppUser appUser = userService.getUser(loggedInUserEmail);

        Optional<PrivateMessageRequest> privateMessageRequestOptional = privateMessageService.getPrivateMessageRequestId(messageId);

        if (privateMessageRequestOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }


        PrivateMessageRequest privateMessageRequest = privateMessageRequestOptional.get();

        if (!privateMessageRequest.getReceiver().getEmail().equals(appUser.getEmail())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();


        }

        privateMessageService.deleteMessageRequest(privateMessageRequest);

        return ResponseEntity.ok(privateMessageRequest);


    }


}
