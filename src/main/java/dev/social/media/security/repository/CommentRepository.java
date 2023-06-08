package dev.social.media.security.repository;

import dev.social.media.security.model.AppUser;
import dev.social.media.security.model.Comment;
import dev.social.media.security.model.Post;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CommentRepository extends MongoRepository<Comment, ObjectId> {
    Optional<Comment> findById(ObjectId id);
    Optional<Comment> findByUser(AppUser appUser);


}
