package de.kiwious.toktik.service;

import de.kiwious.toktik.model.User;
import de.kiwious.toktik.model.Video;
import de.kiwious.toktik.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final VideoService videoService;

    public UserService(UserRepository userRepository, VideoService videoService) {
        this.userRepository = userRepository;
        this.videoService = videoService;
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

    public void likeVideo(String userId, String videoId) {
        User user = getById(userId);

        user.getLikedVideos().add(videoId);
        videoService.likeVideo(videoId);

        userRepository.save(user);
    }

    public User getOrCreate(String discordId, String displayName) {
        return userRepository.findByDiscordId(discordId).orElseGet(() -> {
            User newUser = new User();
            newUser.setDiscordId(discordId);
            newUser.setHandle(displayName);
            return userRepository.save(newUser);
        });
    }
}
