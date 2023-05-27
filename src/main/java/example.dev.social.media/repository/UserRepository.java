package com.example.dev.social.media.repository;

import com.example.dev.social.media.model.AppUser;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository  extends MongoRepository<AppUser, ObjectId> {


    Optional<AppUser> findByEmailIgnoreCase(String email);

}
