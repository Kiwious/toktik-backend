package de.kiwious.toktik.service;

import de.kiwious.toktik.model.User;
import de.kiwious.toktik.model.Video;
import de.kiwious.toktik.repository.UserRepository;
import de.kiwious.toktik.repository.VideoRepository;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final VideoService videoService;
    private final VideoRepository videoRepository;

    public UserService(UserRepository userRepository, VideoService videoService, VideoRepository videoRepository) {
        this.userRepository = userRepository;
        this.videoService = videoService;
        this.videoRepository = videoRepository;
    }

    public User create(User user) {
        return userRepository.insert(user);
    }

    /*public User getOrCreateUser() {

    }*/

    public User getById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new NullPointerException(String.format("user not found by id %s", id)));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void delete(String id) {
        userRepository.deleteById(id);
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }

    public void addWatchedVideo(String userId, String videoId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.getWatchedVideos().add(videoId);

        userRepository.save(user);
    }

    public Video getAndMarkVideoAsWatched(String userId) {
        List<Video> unwatchedVideos = videoService.getUnwatchedVideos(userId);

        Video video = unwatchedVideos.get(0);
        if(video != null) {
            addWatchedVideo(userId, video.getId());
            return video;
        }

        return null;
    }

    public User getPrincipal(Claims claims) {
        String discordId = claims.get("id").toString();

        try {
            return userRepository.findByDiscordId(discordId).orElseGet(() -> {
                User newUser = new User();
                newUser.setDiscordId(discordId);
                newUser.setHandle(claims.get("username").toString());
                newUser.setImageUrl(claims.get("avatar").toString());
                return userRepository.insert(newUser);
            });
        } catch (Exception e) {
            return userRepository.findByDiscordId(discordId)
                    .orElseThrow(() -> new RuntimeException("Failed to create or retrieve user", e));
        }
    }

    public List<Video> getCreatedVideos(String id) {
        return videoRepository.findByAuthorId(id);
    }
}
