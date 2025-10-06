package de.kiwious.toktik.service;

import de.kiwious.toktik.model.video.Comment;
import de.kiwious.toktik.model.user.User;
import de.kiwious.toktik.model.video.Video;
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

    public Video create(Video video, String s3Key, User author) {
        video.setS3Key(s3Key);
        video.setAuthor(author);
        return videoRepository.save(video);
    }

    public List<Video> getAll() {
        List<Video> videos = videoRepository.findAll();
        videos.forEach(video ->
                video.setUrl(s3Service.generatePresignedUrl(video.getS3Key(), Duration.ofHours(1)))
        );
        return videos;
    }


    public Video get(Long videoId) {
        return videoRepository.findById(videoId).orElseThrow(() -> new RuntimeException("No video found"));
    }

    /*public List<Video> getUnwatchedVideos(String userId) {
        User user = userService.getById(userId);
        List<Video> videos = videoRepository.findAll();

        List<String> watched = user.getWatchedVideos() != null ? user.getWatchedVideos() : List.of();

        return videos
                .stream()
                .filter(video -> !watched.contains(video.getId()))
                .toList();
    }*/

    public void deleteAll() {
        videoRepository.deleteAll();
    }

    public void likeVideo(Long videoId) {
        Video video = get(videoId);
        video.setLikes(video.getLikes() + 1);
        videoRepository.save(video);
    }

    public Video addComment(Long videoId, String content, User author) {
        Comment comment = new Comment();
        comment.setId(videoId);
        comment.setContent(content);
        comment.setAuthor(author);

        Video video = videoRepository.findById(videoId).orElseThrow(() -> new RuntimeException("Video not found"));

         video.getComments().add(comment);

        return videoRepository.save(video);
        // commentService.addComment(comment);
    }

    public List<Comment> getComments(Long id) {
        Video video = videoRepository.findById(id).orElseThrow(() -> new RuntimeException("No video found"));
        return video.getComments();
    }

    public List<Long> getAllIds() {
        List<Long> ids = videoRepository.findAll()
                .stream()
                .map(video -> video.getId())
                .collect(Collectors.toList());
        Collections.reverse(ids);
        return ids;
    }

    public Video getVideoById(Long videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new RuntimeException("No video found"));
        video.setUrl(s3Service.generatePresignedUrl(video.getS3Key(), Duration.ofHours(1)));
        return video;
    }
}
