package de.kiwious.toktik.auth;

import de.kiwious.toktik.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Component
public class Oauth2AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JWTUtil jwtUtil;

    @Value("${app.oauth2.redirect-uri:http://localhost:3000}")
    private String REDIRECT_URI = "http://localhost:3000";

    @Value("${app.oauth2.insecure-fragment-fallback:true}")
    private boolean insecureFragmentFallback;

    public Oauth2AuthSuccessHandler(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String token = jwtUtil.generateToken(oAuth2User);
        // Token als HttpOnly Cookie setzen – unterscheide Dev (HTTP) und Prod (HTTPS)
        boolean https = request.isSecure() || "https".equalsIgnoreCase(request.getHeader("X-Forwarded-Proto"));
        String sameSite = https ? "None" : "Lax";

        ResponseCookie cookie = ResponseCookie
                .from("auth_token", token)
                .httpOnly(true)
                .secure(https)
                .path("/")
                .maxAge(Duration.ofHours(24))
                .sameSite(sameSite)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());

        // Fallback: Wenn kein HTTPS -> Token zusätzlich als Fragment übergeben
        String target = REDIRECT_URI;
        if(!https && insecureFragmentFallback) {
            String encoded = URLEncoder.encode(token, StandardCharsets.UTF_8);
            if(!target.endsWith("/")) target += "/";
            target += "auth/callback#token=" + encoded;
        }

        System.out.println("[OAuth2 Success] https=" + https + ", cookieSameSite=" + sameSite + ", redirect=" + target);
        // Redirect zurück zu React
        getRedirectStrategy().sendRedirect(request, response, target);
    }
}
