package com.complaints.api.providers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;

import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

@Component
public class JwtTokenProvider {

  private final SecretKey jwtKey;

  private final long jwtExpirationMs = 86400000; // 1 dia

  public JwtTokenProvider(@Value("${app.jwt.secret}") String secret) {
    this.jwtKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  public String generateToken(Authentication authentication) {
    String cpf = authentication.getName();

    System.out.println("JWT key length: " + jwtKey.getEncoded().length + " bytes");

    return io.jsonwebtoken.Jwts.builder()
        .setSubject(cpf)
        .setIssuedAt(new java.util.Date())
        .setExpiration(new java.util.Date(System.currentTimeMillis() + jwtExpirationMs))
        .signWith(jwtKey, io.jsonwebtoken.SignatureAlgorithm.HS512)
        .compact();
  }

  public String getCpfFromToken(String token) {
    return io.jsonwebtoken.Jwts.parser()
        .setSigningKey(jwtKey)
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }

  public boolean validateToken(String token) {
    try {
      io.jsonwebtoken.Jwts.parser().setSigningKey(jwtKey).parseClaimsJws(token);
      return true;
    } catch (io.jsonwebtoken.JwtException e) {
      return false;
    }
  }
}
