package de.kiwious.toktik.service;

import de.kiwious.toktik.model.User;
import de.kiwious.toktik.model.Video;
import de.kiwious.toktik.repository.VideoRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoService {
    private final VideoRepository videoRepository;
    private final UserService userService;

    public VideoService(VideoRepository videoRepository, @Lazy UserService userService) {
        this.videoRepository = videoRepository;
        this.userService = userService;
    }

    public Video create(Video video) {
        return videoRepository.insert(video);
    }

    public List<Video> getAll() {
        return videoRepository.findAll();
    }

    public Video get(String videoId) {
        return videoRepository.findById(videoId).orElseThrow(() -> new RuntimeException("No video found"));
    }

    public List<Video> getUnwatchedVideos(String userId) {
        User user = userService.getById(userId);
        List<Video> videos = videoRepository.findAll();

        List<String> watched = user.getWatchedVideos() != null ? user.getWatchedVideos() : List.of();

        return videos
                .stream()
                .filter(video -> !watched.contains(video.getId()))
                .toList();
    }

    public void deleteAll() {
        videoRepository.deleteAll();
    }

    public void likeVideo(String videoId) {
        Video video = get(videoId);
        video.setLikes(video.getLikes() + 1);
        videoRepository.save(video);
    }
}
