package dev.social.media.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "follow_request")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowRequest {

    private ObjectId id;
    private AppUser sender;
    private AppUser receiver;
    private LocalDateTime requestTimestamp;

}
