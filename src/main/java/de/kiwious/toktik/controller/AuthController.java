package de.kiwious.toktik.controller;

import de.kiwious.toktik.util.JWTUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final JWTUtil jwtUtil;

    public AuthController(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    private String resolveToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return jwtUtil.extractTokenFromCookie(request);
    }

    @GetMapping("/user")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        String token = resolveToken(request);
        if(token == null || !jwtUtil.isTokenValid(token)) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }
        Claims claims = jwtUtil.extractClaims(token);
        return ResponseEntity.ok(claims);
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }
        return ResponseEntity.ok(authentication.getPrincipal());
    }

    @GetMapping("/status")
    public ResponseEntity<?> checkAuthStatus(HttpServletRequest request) {
        String token = resolveToken(request);
        Map<String, Object> status = new HashMap<>();
        status.put("authenticated", token != null && jwtUtil.isTokenValid(token));
        return ResponseEntity.ok(status);

    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        // Methode 1: Cookie mit leeren Wert überschreiben
        Cookie cookie = new Cookie("auth_token", "");
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // In Production auf true setzen
        cookie.setPath("/");
        cookie.setMaxAge(0); // Sofort löschen
        // WICHTIG: Domain NICHT setzen für localhost!
        // cookie.setDomain("localhost"); // <- Diese Zeile entfernen!

        response.addCookie(cookie);

        // Methode 2: Zusätzlich Set-Cookie Header manuell setzen (fallback)
        response.setHeader("Set-Cookie",
                "auth_token=; Path=/; Max-Age=0; HttpOnly; SameSite=Lax");

        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }
}
