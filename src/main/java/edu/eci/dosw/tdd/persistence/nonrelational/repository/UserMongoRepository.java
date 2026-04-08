package edu.eci.dosw.tdd.persistence.nonrelational.repository;

import edu.eci.dosw.tdd.persistence.nonrelational.document.UserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface UserMongoRepository extends MongoRepository<UserDocument, String> {

    @Query("{ 'username' : ?0 }")
    Optional<UserDocument> findByUsername(String username);

    @Query("{ 'email' : ?0 }")
    Optional<UserDocument> findByEmail(String email);

}
