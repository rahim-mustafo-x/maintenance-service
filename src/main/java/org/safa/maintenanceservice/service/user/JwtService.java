package org.safa.maintenanceservice.service.user;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.safa.maintenanceservice.models.dto.user.auth.AuthUserResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {
    /** Always use this command @command openssl rand -base64 32 this generates random key which u can use in your project where JWT Token is utilized**/

    @Value("${JWT_SECRET_KEY}")
    private String secretKey;

    @Transactional(readOnly = true)
    public AuthUserResponse generateToken(String username, long userId) {
        var key = Keys.hmacShaKeyFor(secretKey.getBytes());
        var accessToken = Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000*60*15))
                .signWith(key)
                .claim("userId", userId)
                .compact();
        var refreshToken = UUID.randomUUID().toString();
        return new AuthUserResponse(accessToken, refreshToken);
    }

    /** Now we are extracting username via saying to the claims to take the subject**/
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    /** In here we are taking the claim which was said **/
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final var claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /** In here we are extracting what was inside the token **/
    private Claims extractAllClaims(String token) {
        var key = Keys.hmacShaKeyFor(secretKey.getBytes());
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /** We are checking if token is valid username is in place and expiration is validated **/
    public boolean validateToken(String token, UserDetails details) {
        final var username = extractUsername(token);
        return (username.equals(details.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}