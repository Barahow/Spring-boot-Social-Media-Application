package dev.social.media.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "like")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Like {
    @Id
    private ObjectId id;

    private AppUser user;

    private Post post;

    private Comment comment;

    private LocalDateTime createdAt;





}



