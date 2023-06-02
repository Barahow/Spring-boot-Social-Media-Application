package dev.social.media.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import dev.social.media.security.config.EnvironmentKey;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
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
        return access_token;

    }
    public String getUserIdFromToken(String token) {
        Key key = Keys.hmacShaKeyFor(environmentKey.getSecretKey().getBytes());

        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build().parseClaimsJws(token);

        return claims.getBody().getSubject();


    }
}

