package dev.social.media.security.repository;


import dev.social.media.security.model.AppUser;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository  extends MongoRepository<AppUser, ObjectId> {


    AppUser findByEmailIgnoreCase(String email);

}
