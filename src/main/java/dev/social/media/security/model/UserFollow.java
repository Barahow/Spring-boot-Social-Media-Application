package dev.social.media.security.model;
import dev.social.media.security.model.AppUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "user_follows")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFollow {
    @Id
    private String id;

    private AppUser follower; // Follower user object
    private AppUser followedUser; // Followed user object
    private LocalDateTime createdAt;

    // Constructors, getters, and setters

    // ...
}

