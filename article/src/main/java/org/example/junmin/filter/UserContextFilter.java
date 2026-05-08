package org.example.junmin.filter;

import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.junmin.config.UserContext;
import org.example.junmin.service.UserClient;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class UserContextFilter extends OncePerRequestFilter {
    @Resource
    private UserClient userClient;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            String username = request.getHeader("X-User");
            if (username != null) {
                Long userId = userClient.getUserById(username);
                if (userId != null) {
                    UserContext.setUser(userId);
                }
            }
            filterChain.doFilter(request, response);

        } finally {
            UserContext.clear();
        }
    }
}