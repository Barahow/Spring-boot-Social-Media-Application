package dev.social.media.security.controller;


import dev.social.media.security.dto.PostContent;
import dev.social.media.security.model.AppUser;
import dev.social.media.security.model.Post;
import dev.social.media.security.service.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/api/v1/hashtag")
public class HashTagController {

    private final HashTagService hashTagService;

    private final UserService userService;
    private final SearchService searchService;

    private final JwtTokenProvider jwtTokenProvider;

    private SimpMessagingTemplate messagingTemplate;



    @PostMapping
    public ResponseEntity<Post> createHashTags(@RequestBody @Valid PostContent postContent, @RequestHeader("Authorization") String authorizationHeader) {
        String loggedInUserEmail = jwtTokenProvider.getEmailFromToken(authorizationHeader);

        AppUser appUser = userService.getUser(loggedInUserEmail);

        if (appUser== null) {
            return ResponseEntity.notFound().build();
        }

        if (!appUser.getEmail().equals(loggedInUserEmail)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }


        List<String> hashTag = postContent.getHashTag().stream()
                .map(tag -> "@" + tag)
                .collect(Collectors.toList());

// Call the service method with the modified hashtags
      Post post=  hashTagService.createHashTag(appUser.getEmail(),hashTag );



        messagingTemplate.convertAndSend("/topic/post",post);


        return new ResponseEntity<>(post,HttpStatus.CREATED);

    }







    @GetMapping("/{tag}")
    public ResponseEntity<List<Post>> searchHashTag(@PathVariable("tag")  List<String> hashTag  ) {

        List<Post> hashTagList = searchService.searchPostByHashTag(hashTag);






        if (hashTagList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }






        return new ResponseEntity<>(hashTagList,HttpStatus.CREATED);

    }





}
