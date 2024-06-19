package com.sparta.easyspring.auth.util;

import com.sparta.easyspring.auth.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final long tokenExpiration;
    private final long refreshTokenExpiration;
    private final SecretKey secretKey;

    public JwtUtil(JwtConfig jwtConfig){
        this.tokenExpiration = jwtConfig.getTokenExpiration();
        this.refreshTokenExpiration = jwtConfig.getRefreshTokenExpiration();
        this.secretKey = Keys.hmacShaKeyFor(jwtConfig.getSecretKey().getBytes());
    }

    /**
     * 사용자 이름으로 토큰 발급
     * @param username
     * @return
     */
    public String createAccessToken(String username){
        return generateToken(username,tokenExpiration);
    }
    public String createRefreshToken(String username){
        return generateToken((username), refreshTokenExpiration);
    }

    /**
     * 토큰 생성
     * @param username
     * @param expiration
     * @return
     */
    public String generateToken(String username,long expiration){
        return Jwts.builder()
            .setSubject(username) // 토큰 발행 주체
            .setIssuedAt(new Date()) // 토큰 발행 시간
            .setExpiration(new Date(System.currentTimeMillis()+expiration)) // 토큰 만료 시간
            .signWith(secretKey,SignatureAlgorithm.HS256)
            .compact();
    }


    /**
     * JWT 토큰에서 Claims추출 (사용자 정보)
     * @param token
     * @return
     */
    public Claims extractClaims(String token){
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    public boolean validateToken(String token){
        try{
            extractClaims(token);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public String getUsernameFromToken(String token){
        return extractClaims(token).getSubject();
    }
}
