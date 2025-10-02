package de.kiwious.toktik.repository;

import de.kiwious.toktik.model.Video;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends MongoRepository<Video, String> {
    List<Video> findByAuthorId(String authorId);
}
