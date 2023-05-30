package example.dev.social.media.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "follow")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Follow {

    @Id
    private ObjectId id;
    @DBRef
    private AppUser follower;
    private String followingUserId;

    private LocalDateTime createdAt;

}
