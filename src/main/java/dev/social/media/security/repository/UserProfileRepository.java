package dev.social.media.security.repository;

import dev.social.media.security.model.AppUser;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserProfileRepository extends MongoRepository<AppUser, ObjectId> {
    AppUser findByEmailIgnoreCase(String email);

}
