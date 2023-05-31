package dev.social.media.security.repository;

import dev.social.media.security.model.UserRole;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends MongoRepository<UserRole, ObjectId> {

    UserRole findByName(String name);
}
