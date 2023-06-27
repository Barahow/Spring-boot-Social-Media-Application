package dev.social.media.security.repository;


import dev.social.media.security.model.AppUser;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository  extends MongoRepository<AppUser, ObjectId> {


    AppUser findByEmailIgnoreCase(String email);

    AppUser findByUserName(String userName);



    List<AppUser> findByUserNameRegex(String s, String i);
}
