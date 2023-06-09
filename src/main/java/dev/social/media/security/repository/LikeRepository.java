package dev.social.media.security.repository;

import dev.social.media.security.model.AppUser;
import dev.social.media.security.model.Like;
import dev.social.media.security.model.Post;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends MongoRepository<Like, ObjectId> {
    Optional<Like> findById(ObjectId id);

    Optional<Like> findByUser(AppUser appUser);


}
