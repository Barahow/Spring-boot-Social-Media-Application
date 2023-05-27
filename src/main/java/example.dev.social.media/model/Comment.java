package com.example.dev.social.media.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "comment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    @Id
    private ObjectId id;
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updateAt;

    @DBRef
    private AppUser user;
    @DBRef
    private Post post;





}
