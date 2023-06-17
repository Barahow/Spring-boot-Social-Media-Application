package dev.social.media.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
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
        String secret = System.getenv("MY_APP_SECRET_KEY");
        log.info("Secret key value is: " + secret);
        List<String> roles =user.getAuthorities().stream().map(GrantedAuthority:: getAuthority).collect(Collectors.toList());

      //  Claims claims = Jwts.claims().setSubject(user.getUsername());
        //claims.put("roles", roles);

       // SecretKey secretKey = Keys.hmacShaKeyFor(environmentKey.getSecretKey().getBytes());

        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());

        String access_token = JWT.create().withSubject(user.getUsername())
                .withClaim("roles",roles)
                .withIssuer(request.getRequestURI())
                .withExpiresAt(new Date(System.currentTimeMillis()+60*60*1000))
                .sign(algorithm);

        log.info("access Token  {}", access_token);

        return access_token;

    }
    public boolean validateToken(String token) {
        try {

            String secretKey = System.getenv("MY_APP_SECRET_KEY");
            Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException ex) {
            log.error("Token verification failed: {}", ex.getMessage());
            return false;
        }
    }
    public String getEmailFromToken(String token) {
        String secretKey = System.getenv("MY_APP_SECRET_KEY");
        log.info("secret key {}",secretKey);
        //Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
        Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes());
        JWTVerifier verify = JWT.require(algorithm).build();

        log.info(token);
        String tokenWithoutBearer = token.replace("Bearer ", "");
        DecodedJWT decodedJWT = verify.verify(tokenWithoutBearer);



        return decodedJWT.getSubject();


    }
}

