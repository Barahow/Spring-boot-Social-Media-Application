package dev.social.media.security.repository;

import dev.social.media.security.model.Follow;
import dev.social.media.security.model.FollowRequest;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRequestRepository extends MongoRepository<FollowRequest,ObjectId> {


}
