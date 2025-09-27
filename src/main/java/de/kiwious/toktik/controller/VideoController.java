package de.kiwious.toktik.controller;

import de.kiwious.toktik.model.Video;
import de.kiwious.toktik.service.VideoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class VideoController {
    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @PostMapping("/video")
    public Video post(@RequestBody Video video) {
        return videoService.create(video);
    }

    @GetMapping("/video")
    public List<Video> getAll() {
        return videoService.getAll();
    }

    // TODO: implement put endpoint
    @PutMapping("/video/{id}")
    public Video put(@PathVariable String id, @RequestBody Video updatedVideo) {
        Video video = videoService.get(id);
        return updatedVideo;
    }

    @DeleteMapping("/video")
    public void deleteAll() {
        videoService.deleteAll();
    }
}
