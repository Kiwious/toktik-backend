package de.kiwious.toktik.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JWTUtil {

    // 32-byte key for HS256 (256 bits)
    private static final String SECRET_KEY = "01234567890123456789012345678901";

    // Token validity: 1 day
    private static final long EXPIRATION_MS = 24 * 60 * 60 * 1000;

    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    public static String generateToken(String userId, String username) {
        try {
            return Jwts.builder()
                    .setSubject(userId)
                    .claim("username", username)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                    .signWith(KEY, SignatureAlgorithm.HS256)
                    .compact();
        } catch (JwtException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getUserId(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(KEY)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }
}
