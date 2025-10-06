package de.kiwious.toktik.service;

import de.kiwious.toktik.model.video.Comment;
import de.kiwious.toktik.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> getCommentsForVideo(Long videoId) {
        return commentRepository.findByVideoId(videoId);
    }

    public Comment addComment(Comment comment) {
       return commentRepository.save(comment);
    }
}
