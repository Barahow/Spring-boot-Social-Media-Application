package dev.social.media.security.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "post")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    private ObjectId id;
    @NotBlank(message = "the content must not be blank")
    @Size(max = 500,message = "the content must not exceed 500 characters")
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @DBRef
    private AppUser user;



}
