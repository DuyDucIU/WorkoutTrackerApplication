package com.duyduc.workout_tracker.security;

import com.nimbusds.jwt.proc.BadJWTException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = jwtUtils.getTokenFromRequest(request);

        try {
            if (StringUtils.hasText(token) && jwtUtils.validateToken(token)) {
                String username = jwtUtils.getUsernameFromToken(token);
                Integer userId = jwtUtils.getUserIdFromToken(token);

                // Create UserPrincipal from JWT claims
                UserPrincipal userDetails = new UserPrincipal(userId, username, null);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        filterChain.doFilter(request, response);

        } catch (AuthenticationException e) {
            SecurityContextHolder.clearContext();
            throw e;
        }
    }
}
