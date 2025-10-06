package de.kiwious.toktik.controller;

import de.kiwious.toktik.dto.CommentDTO;
import de.kiwious.toktik.model.video.Comment;
import de.kiwious.toktik.model.user.User;
import de.kiwious.toktik.model.video.Video;
import de.kiwious.toktik.service.CommentService;
import de.kiwious.toktik.service.VideoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/comment")
public class CommentController {

    private final VideoService videoService;
    private final CommentService commentService;

    public CommentController(VideoService videoService, CommentService commentService) {
        this.videoService = videoService;
        this.commentService = commentService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<Comment> newComment(@PathVariable Long id, @RequestBody CommentDTO commentDTO, @AuthenticationPrincipal User principal) {
        System.out.println("Adding comment to video ID: " + id + " by user: " + principal.getId());
        Comment comment = videoService.addComment(id, commentDTO.getContent(), principal);
        return ResponseEntity.ok(comment);
    }

    @GetMapping("/{id}")
    public List<Comment> getCommentsForVideo(@PathVariable Long id) {
        return commentService.getCommentsForVideo(id);
    }
}
