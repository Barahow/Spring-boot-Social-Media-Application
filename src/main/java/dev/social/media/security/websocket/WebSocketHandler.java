package dev.social.media.security.websocket;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Objects;

@Service
@AllArgsConstructor
@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();

        String destination = Objects.requireNonNull(session.getUri()).getPath();
        // Check the message type
        if ("/topic/messages".equals(destination)) {
            handlePrivateMessage(payload);
        } else if ("/topic/notifications".equals(destination)) {
            handleNotification(payload);
        }else if ("/topic/post".equals(destination)) {
            handleNewPostFromUser(payload);
        }else if ("/topic/comment".equals(destination)) {
            handleNewCommentFromUser(payload);
        } else {
            // Handle other types of messages
        }
    }

    private void handlePrivateMessage(String message) {

        messagingTemplate.convertAndSend("/topic/messages",message);

    }


    private void handleNotification(String message) {

        messagingTemplate.convertAndSend("/topic/notifications",message);
    }

    private void handleNewPostFromUser(String message) {

        messagingTemplate.convertAndSend("/topic/post",message);
    }

    private void handleNewCommentFromUser(String message) {

        messagingTemplate.convertAndSend("/topic/comment",message);
    }
}


