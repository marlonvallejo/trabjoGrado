package com.hotelandinocode.hotelandino.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String jwtSecret;

    public String createToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 3600000);  // El token expira en 1 hora

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
}
