package edu.eci.dosw.tdd.persistence.nonrelational.repository;

import edu.eci.dosw.tdd.persistence.nonrelational.document.BookDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface BookMongoRepository extends MongoRepository<BookDocument, String> {

    @Query("{ 'title' : ?0 }")
    BookDocument findItemByName(String title);

    @Query(value="{ 'author' : ?0 }", fields="{'title' : 1, 'isbn' : 1}")
    List<BookDocument> findAllBy(String author);

}
