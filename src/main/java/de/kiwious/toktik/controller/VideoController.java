package de.kiwious.toktik.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.kiwious.toktik.model.User;
import de.kiwious.toktik.model.Video;
import de.kiwious.toktik.service.FileUploadService;
import de.kiwious.toktik.service.VideoService;
import de.kiwious.toktik.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class VideoController {
    private final VideoService videoService;
    private final FileUploadService fileUploadService;
    private final JWTUtil jwtUtil;

    public VideoController(VideoService videoService, FileUploadService fileUploadService, JWTUtil jwtUtil) {
        this.videoService = videoService;
        this.fileUploadService = fileUploadService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/video")
    public ResponseEntity<Video> post(
            @AuthenticationPrincipal User user,
            @RequestParam("file") MultipartFile file,
            @RequestParam("video") String videoJson,
            HttpServletRequest request
    ) throws IOException {

        String token = jwtUtil.extractTokenFromCookie(request);
        if(!jwtUtil.isTokenValid(token)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

        ObjectMapper objectMapper = new ObjectMapper();
        Video video = objectMapper.readValue(videoJson, Video.class);
        MultipartFile multipartFile = fileUploadService.upload(file);

        Video createdVideo = videoService.create(video, multipartFile.getOriginalFilename(), user);

        return ResponseEntity.ok(createdVideo);
    }

    @GetMapping("/video")
    public List<Video> getAll() {
        return videoService.getAll();
    }

    @GetMapping("/video/{id}")
    public Video get(@PathVariable String id) { return videoService.getVideoById(id); }

    // TODO: implement put endpoint
    @PutMapping("/video/{id}")
    public Video put(@PathVariable String id, @RequestBody Video updatedVideo) {
        Video video = videoService.get(id);
        return updatedVideo;
    }

    @GetMapping("/video/ids")
    public List<String> getIds() {
        return videoService.getAllIds();
    }

    @DeleteMapping("/video")
    public void deleteAll() {
        videoService.deleteAll();
    }
}
