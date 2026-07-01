package org.safa.maintenanceservice.interceptor;

import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.safa.maintenanceservice.service.RateLimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {
    @Autowired
    private RateLimitService service;

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        var sessionKey = request.getSession(true).getId();
        var httpMethod = request.getMethod();
        var uri = request.getRequestURI();
        Bucket bucket;
        //Only when changing the necessary data like login,register,delete,update, or even search as well as post, put, patch, delete
        if (uri.contains("/login") || uri.contains("/register") || uri.contains("/refreshToken") || uri.contains("/log-out") || httpMethod.equalsIgnoreCase("POST") ||  httpMethod.equalsIgnoreCase("PUT") || httpMethod.equalsIgnoreCase("DELETE") || httpMethod.equalsIgnoreCase("PATCH")) {
            bucket = service.resolveStrictBucket(sessionKey);
        } else if (uri.contains("/scroll")) {
            bucket = service.resolveScrollBucket(sessionKey);
        }else {
            //this is for a user whose habit is to go back and forth for getById or anything similar uri
            bucket = service.resolveReguralBucket(sessionKey);
        }
        if (bucket.tryConsume(1)){
            //if the bucket has something in it
            return true;
        }else {
            //if the bucket is already empty
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("\"error\": \"Too Many Requests, please wait!\" ");
            return false;
        }
    }
}
