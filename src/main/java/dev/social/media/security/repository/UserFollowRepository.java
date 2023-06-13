package dev.social.media.security.repository;

import dev.social.media.security.model.AppUser;
import dev.social.media.security.model.Follow;
import dev.social.media.security.model.UserFollow;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserFollowRepository  extends MongoRepository<UserFollow, ObjectId> {

    Optional<UserFollow> findById(ObjectId id);
     Optional<List<UserFollow>> findByFollower(AppUser user);
    Optional<List<UserFollow>> findByFollowedUser(AppUser user);


}
