package dev.social.media.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import dev.social.media.security.config.EnvironmentKey;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.util.Daemon;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.List;

import java.nio.file.attribute.UserPrincipal;
import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Service
public class JwtTokenProvider {

    private EnvironmentKey environmentKey;

    private int jwtexpirationInMs;

    public String generateAccessToken(Authentication authentication, HttpServletRequest request) {
     User user = (User) authentication.getPrincipal();

        List<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();

        Claims claims = Jwts.claims().setSubject(user.getUsername());
        claims.put("roles", roles);

        SecretKey secretKey = Keys.hmacShaKeyFor(environmentKey.getSecretKey().getBytes());

        Algorithm algorithm = Algorithm.HMAC256(secretKey.getAlgorithm());

        String access_token = JWT.create().withSubject(user.getUsername())
                .withClaim("roles",user.getAuthorities().stream().map(GrantedAuthority:: getAuthority).collect(Collectors.toList()))
                .withIssuer(request.getRequestURI().toString())
                .withExpiresAt(new Date(System.currentTimeMillis()+60*60*1000))
                .sign(algorithm);

        log.info("access Token  {}", access_token);

        return access_token;

    }
    public boolean validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(environmentKey.getSecretKey().getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException ex) {
            log.error("Token verification failed: {}", ex.getMessage());
            return false;
        }
    }
    public String getEmailFromToken(String token) {
        Key key = Keys.hmacShaKeyFor(environmentKey.getSecretKey().getBytes());

        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build().parseClaimsJws(token);

        return claims.getBody().get("email", String.class);


    }
}

