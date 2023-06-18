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
import java.util.List;
@Document(collection = "private_message")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrivateMessage {
    @Id
    private ObjectId id;

    @NotBlank(message = "The content must not be blank")
    @Size(max = 500, message = "The content must not exceed 500 characters")
    private String content;

    private List<String> pictureGallery;
    private List<String> gif;

    private LocalDateTime createdAt;

    @DBRef
    private AppUser sender;

    @DBRef
    private AppUser receiver;
}
