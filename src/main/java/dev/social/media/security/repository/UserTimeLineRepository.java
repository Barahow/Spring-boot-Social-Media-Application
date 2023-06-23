package dev.social.media.security.repository;

import dev.social.media.security.model.AppUser;
import dev.social.media.security.model.Comment;
import dev.social.media.security.model.UserTimeLine;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTimeLineRepository extends MongoRepository<UserTimeLine, ObjectId> {

        Optional<UserTimeLine> findByUser(AppUser appUser);


    }


