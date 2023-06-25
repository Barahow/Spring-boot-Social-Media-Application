package dev.social.media.security.repository;

import dev.social.media.security.model.AppUser;
import dev.social.media.security.model.Post;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

// template to remember
/*
private ObjectId id;
private String content;
private LocalDateTime createdAt;
private LocalDateTime updatedAt;

private AppUser user;*/
@Repository
public interface PostRepository extends MongoRepository< Post, ObjectId> {
    @Override
    Optional<Post> findById(ObjectId id);

    Optional<Post> findByUser(AppUser appUser);

  //  Optional<Post> findByUserName(String userName);

    @Override
    void deleteById(ObjectId id);

    List<Post> findByUserIn(List<AppUser> followingUsers);
}
