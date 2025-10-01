package de.kiwious.toktik.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.kiwious.toktik.model.Video;
import de.kiwious.toktik.service.FileUploadService;
import de.kiwious.toktik.service.VideoService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class VideoController {
    private final VideoService videoService;
    private final FileUploadService fileUploadService;

    public VideoController(VideoService videoService, FileUploadService fileUploadService) {
        this.videoService = videoService;
        this.fileUploadService = fileUploadService;
    }

    @PostMapping("/video")
    public Video post(
            @RequestParam("file") MultipartFile file,
            @RequestParam("video") String videoJson) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        Video video = objectMapper.readValue(videoJson, Video.class);
        MultipartFile multipartFile = fileUploadService.upload(file);

        return videoService.create(video, multipartFile.getOriginalFilename());
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
