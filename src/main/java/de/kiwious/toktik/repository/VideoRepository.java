package de.kiwious.toktik.repository;

import de.kiwious.toktik.model.video.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    List<Video> findByAuthorId(Long authorId);
}
