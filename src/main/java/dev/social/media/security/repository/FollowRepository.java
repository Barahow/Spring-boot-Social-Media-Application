package dev.social.media.security.repository;

import dev.social.media.security.model.AppUser;
import dev.social.media.security.model.Comment;
import dev.social.media.security.model.Follow;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import java.util.List;

@Repository
public interface FollowRepository  extends MongoRepository<Follow, ObjectId> {
    Optional<Follow> findById(ObjectId id);
    Optional<List<Follow>> findByFollower(AppUser user);

}
