package de.kiwious.toktik.service;

import de.kiwious.toktik.model.Comment;
import de.kiwious.toktik.model.User;
import de.kiwious.toktik.model.Video;
import de.kiwious.toktik.repository.VideoRepository;
import de.kiwious.toktik.service.aws.S3Service;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VideoService {
    private final VideoRepository videoRepository;

    private final UserService userService;
    private final S3Service s3Service;

    public VideoService(VideoRepository videoRepository, @Lazy UserService userService, S3Service s3Service) {
        this.videoRepository = videoRepository;
        this.userService = userService;
        this.s3Service = s3Service;
    }

    public Video create(Video video, String s3Key) {
        video.setS3Key(s3Key);
        return videoRepository.insert(video);
    }

    public List<Video> getAll() {
        List<Video> videos = videoRepository.findAll();
        videos.forEach(video ->
                video.setUrl(s3Service.generatePresignedUrl(video.getS3Key(), Duration.ofHours(1)))
        );
        return videos;
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

    public void addComment(String videoId, String authorId, String content) {
        Comment comment = new Comment();
        User author = userService.getById(authorId);
        comment.setVideoId(videoId);
        comment.setContent(content);
        comment.setAuthor(author);

        Video video = videoRepository.findById(videoId).orElseThrow(() -> new RuntimeException("Video not found"));

        video.getComments().add(comment);

        videoRepository.save(video);
        // commentService.addComment(comment);
    }

    public List<Comment> getComments(String id) {
        Video video = videoRepository.findById(id).orElseThrow(() -> new RuntimeException("No video found"));
        return video.getComments();
    }

    public List<String> getAllIds() {
        List<String> ids = videoRepository.findAll()
                .stream()
                .map(video -> video.getId())
                .collect(Collectors.toList());
        Collections.reverse(ids);
        return ids;
    }

    public Video getVideoById(String videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new RuntimeException("No video found"));
        video.setUrl(s3Service.generatePresignedUrl(video.getS3Key(), Duration.ofHours(1)));
        return video;
    }
}
