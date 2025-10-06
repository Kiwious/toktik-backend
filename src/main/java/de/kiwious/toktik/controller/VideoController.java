package de.kiwious.toktik.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.kiwious.toktik.model.user.User;
import de.kiwious.toktik.model.video.Video;
import de.kiwious.toktik.service.FileUploadService;
import de.kiwious.toktik.service.VideoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class VideoController {
    private final VideoService videoService;
    private final FileUploadService fileUploadService;

    public VideoController(VideoService videoService, FileUploadService fileUploadService) {
        this.videoService = videoService;
        this.fileUploadService = fileUploadService;
    }

    @PostMapping("/video")
    public ResponseEntity<Video> post(
            @AuthenticationPrincipal User user,
            @RequestParam("file") MultipartFile file,
            @RequestParam("video") String videoJson
    ) throws IOException, InterruptedException {
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
    public Video get(@PathVariable Long id) { return videoService.getVideoById(id); }

    @GetMapping("/video/ids")
    public List<Long> getIds() {
        return videoService.getAllIds();
    }

    @DeleteMapping("/video")
    public void deleteAll() {
        videoService.deleteAll();
    }
}
