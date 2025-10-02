package de.kiwious.toktik.controller;

import de.kiwious.toktik.dto.CommentDTO;
import de.kiwious.toktik.model.Comment;
import de.kiwious.toktik.model.User;
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

    public CommentController(VideoService videoService) {
        this.videoService = videoService;
    }

    // TODO: infer user from auth
    @PostMapping("/{id}")
    public ResponseEntity<Void> newComment(@PathVariable String id, @RequestBody CommentDTO commentDTO, @AuthenticationPrincipal User principal) {
        videoService.addComment(id, commentDTO.getAuthorId(), commentDTO.getContent(), principal);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public List<Comment> getCommentsForVideo(@PathVariable String id) {
        return videoService.getComments(id);
    }
}
