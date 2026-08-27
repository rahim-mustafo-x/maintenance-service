package org.safa.maintenanceservice.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.safa.maintenanceservice.models.dto.ApiResponse;
import org.safa.maintenanceservice.service.user.JwtService;
import org.safa.maintenanceservice.service.user.MaintenanceUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private ApplicationContext context;
    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            var authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                var token = authHeader.substring(7);
                var username = jwtService.extractUsername(token);
                if (username != null && SecurityContextHolder.getContext().getAuthentication()== null) {
                    UserDetails details = context.getBean(MaintenanceUserDetailsService.class).loadUserByUsername(username);
                    if (jwtService.validateToken(token, details)){
                        var authToken = new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            }else{
                if (request.getRequestURI().contains("/v1")){
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    var responseBody = ApiResponse.builder()
                            .code(HttpStatus.UNAUTHORIZED.value())
                            .message("You are not logged in")
                            .build();
                    ObjectMapper mapper = new ObjectMapper();
                    response.getWriter().write(mapper.writeValueAsString(responseBody));
                    return;
                }
            }
            filterChain.doFilter(request, response);
        }catch (ExpiredJwtException _){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            var responseBody = ApiResponse.builder()
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .message("You are not logged in")
                    .build();
            ObjectMapper mapper = new ObjectMapper();
            response.getWriter().write(mapper.writeValueAsString(responseBody));
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/auth");
    }
}
