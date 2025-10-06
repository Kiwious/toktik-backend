package de.kiwious.toktik.auth;

import de.kiwious.toktik.model.user.User;
import de.kiwious.toktik.service.UserService;
import de.kiwious.toktik.util.JWTUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JWTUtil jWTUtil;
    private final UserService userService;

    public JwtAuthFilter(JWTUtil jWTUtil, UserService userService) {
        this.jWTUtil = jWTUtil;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jWTUtil.extractTokenFromCookie(request);

        if(token != null && jWTUtil.isTokenValid(token)) {
            Claims claims = jWTUtil.extractClaims(token);

            User user = userService.getPrincipal(claims);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

}
