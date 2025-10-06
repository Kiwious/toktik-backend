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

    private final S3Service s3Service;
    private final CommentService commentService;

    public VideoService(VideoRepository videoRepository, S3Service s3Service, CommentService commentService) {
        this.videoRepository = videoRepository;
        this.commentService = commentService;
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

    public Comment addComment(Long videoId, String content, User author) {
        Video video = videoRepository.findById(videoId).orElseThrow(() -> new RuntimeException("Video not found"));
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setAuthor(author);
        comment.setVideo(video);

        return commentService.addComment(comment);
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
