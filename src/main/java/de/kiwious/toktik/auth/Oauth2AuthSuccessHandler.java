package de.kiwious.toktik.auth;

import de.kiwious.toktik.util.JWTUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

@Component
public class Oauth2AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JWTUtil jwtUtil;

    @Value("${app.oauth2.redirect-uri:http://localhost:3000}")
    private String REDIRECT_URI = "http://217.234.136.61:3000";

    public Oauth2AuthSuccessHandler(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        System.out.println("REDIRECT_URI: " + REDIRECT_URI);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String token = jwtUtil.generateToken(oAuth2User);

        // Token als HttpOnly Cookie setzen
        Cookie cookie = new Cookie("auth_token", token);
        cookie.setHttpOnly(true);  // JavaScript kann nicht darauf zugreifen
        cookie.setSecure(false);   // Für Production auf true setzen (HTTPS only)
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60); // 24 Stunden
        // cookie.setDomain("localhost"); // Für Production anpassen

        response.addCookie(cookie);

        // Redirect zurück zu React (ohne Token in URL!)
        getRedirectStrategy().sendRedirect(request, response, REDIRECT_URI);
    }
}
