package dev.social.media.security.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.security.core.Transient;

import java.time.LocalDateTime;

@Transient
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notifications {

    @Id
    private ObjectId id;
    private String notificationMessage;
    @DBRef
    private AppUser sender;
    @DBRef
    private AppUser receiver;

    private LocalDateTime createdAt;

    private boolean isRead;

}
