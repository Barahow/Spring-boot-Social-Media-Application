package dev.social.media.security.repository;

import dev.social.media.security.model.PrivateMessageRequest;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivateMessageRequestRepository extends MongoRepository<PrivateMessageRequest, ObjectId> {
}
