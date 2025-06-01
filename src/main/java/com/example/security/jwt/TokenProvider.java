package com.example.security.jwt;

import com.example.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class TokenProvider {

  private static final String SECRET_KEY = "YourSuperSecretKeyYourSuperSecretKey"; // At least 256-bit key
  private static final long TOKEN_VALIDITY = 86400000L;
  private static final String AUTHORITIES_KEY = "auth";
  private static final String USER_EMAIL = "email";
  private static final String USER_FIRST_NAME = "first_name";
  private static final String USER_LAST_NAME = "last_name";

  private final SecretKey secretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

  public String createToken(Authentication authentication, User user) {
    String authorities = authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(","));

    Date currentDate = new Date();
    Date expiryDate = new Date(currentDate.getTime() + TOKEN_VALIDITY);

    return Jwts.builder()
        .setSubject(authentication.getName())
        .claim(AUTHORITIES_KEY, authorities)
        .claim(USER_EMAIL, user.getEmail())
        .claim(USER_FIRST_NAME, user.getFirstName())
        .claim(USER_LAST_NAME, user.getLastName())
        .setIssuedAt(currentDate)
        .setExpiration(expiryDate)
        .signWith(secretKey, SignatureAlgorithm.HS256)
        .compact();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(secretKey)
          .build()
          .parseClaimsJws(token);
      return true;
    } catch (Exception e) {
      throw new AuthenticationCredentialsNotFoundException(
          "JWT token is not valid: " + e.getMessage());
    }
  }

  public String getUsernameFromToken(String token) {
    Claims claims = Jwts.parserBuilder()
        .setSigningKey(secretKey)
        .build()
        .parseClaimsJws(token)
        .getBody();
    return claims.getSubject();
  }

  public String getAuthoritiesFromToken(String token) {
    Claims claims = Jwts.parserBuilder()
        .setSigningKey(secretKey)
        .build()
        .parseClaimsJws(token)
        .getBody();
    return claims.get(AUTHORITIES_KEY, String.class);
  }

}
