package dev.social.media.security.repository;

import dev.social.media.security.model.PrivateMessage;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivateMessageRepository extends MongoRepository<PrivateMessage, ObjectId> {

}
