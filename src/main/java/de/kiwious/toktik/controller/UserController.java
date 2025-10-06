package de.kiwious.toktik.controller;

import de.kiwious.toktik.model.user.User;
import de.kiwious.toktik.model.video.Video;
import de.kiwious.toktik.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/video/{id}")
    public List<Video> getVideoForUser(@PathVariable String id) {
        return userService.getCreatedVideos(id);
    }

    @PostMapping("/user")
    public User post(@RequestBody User user) {
        return userService.create(user);
    }

    @GetMapping("/user/{id}")
    public User getById(@PathVariable String id) {
        return userService.getById(id);
    }

    @GetMapping("/user")
    public List<User> getAll() {
        return userService.getAllUsers();
    }

    // delete
    @DeleteMapping("/user/{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {
        userService.delete(id);
        return ResponseEntity.ok("User deleted");
    }

    @DeleteMapping("/user")
    public void deleteAll() {
        userService.deleteAll();
    }

    @GetMapping("/user/me")
    public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(user);
    }

}
