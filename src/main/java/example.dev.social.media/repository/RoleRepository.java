package example.dev.social.media.repository;

import example.dev.social.media.model.UserRole;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends MongoRepository<UserRole, ObjectId> {

    UserRole findByName(String name);
}
