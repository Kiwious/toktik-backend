package de.kiwious.toktik.service;

import de.kiwious.toktik.model.user.User;
import de.kiwious.toktik.model.video.Video;
import de.kiwious.toktik.repository.UserRepository;
import de.kiwious.toktik.repository.VideoRepository;
import de.kiwious.toktik.util.JWTUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final VideoRepository videoRepository;
    private final JWTUtil jWTUtil;

    public UserService(UserRepository userRepository, VideoRepository videoRepository, JWTUtil jWTUtil) {
        this.userRepository = userRepository;
        this.videoRepository = videoRepository;
        this.jWTUtil = jWTUtil;
    }

    public User create(User user) {
        return userRepository.save(user);
    }

    /*public User getOrCreateUser() {

    }*/

    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NullPointerException(String.format("user not found by id %s", id)));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }

    public void addWatchedVideo(Long userId, Long videoId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        // user.getWatchedVideos().add(videoId);

        userRepository.save(user);
    }

    /*public Video getAndMarkVideoAsWatched(String userId) {
        List<Video> unwatchedVideos = videoService.getUnwatchedVideos(userId);

        Video video = unwatchedVideos.get(0);
        if(video != null) {
            addWatchedVideo(userId, video.getId());
            return video;
        }

        return null;
    }*/

    public User getPrincipal(Claims claims) {
        String discordId = claims.get("id").toString();

        try {
            return userRepository.findByDiscordId(discordId).orElseGet(() -> {
                User newUser = new User();
                newUser.setDiscordId(discordId);
                newUser.setHandle(claims.get("username").toString());
                newUser.setImageUrl(claims.get("avatar").toString());
                return userRepository.save(newUser);
            });
        } catch (Exception e) {
            return userRepository.findByDiscordId(discordId)
                    .orElseThrow(() -> new RuntimeException("Failed to create or retrieve user", e));
        }
    }

    public List<Video> getCreatedVideos(Long id) {
        return videoRepository.findByAuthorId(id);
    }
}
