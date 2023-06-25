package dev.social.media.security.controller;

import dev.social.media.security.model.Post;
import dev.social.media.security.service.CommentService;
import dev.social.media.security.service.PostService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import java.util.List;

@Controller
@Slf4j
@AllArgsConstructor
public class WebSocketController {
    private final SimpMessagingTemplate messagingTemplate;
    private final PostService postService;
    private final CommentService commentService;


    @SubscribeMapping("/topic/post")
    public List<Post> handlePostSubscription() {
        // Logic to fetch and return the initial posts
        List<Post> initialPosts = postService.getInitialPosts();

        if (initialPosts== null) {
            throw new NullPointerException("No post yet");
        }
        return initialPosts;
    }
}
