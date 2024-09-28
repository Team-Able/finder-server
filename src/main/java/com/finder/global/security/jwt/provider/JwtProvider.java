package com.finder.global.security.jwt.provider;

import com.finder.domain.user.domain.entity.UserEntity;
import com.finder.global.security.jwt.config.JwtProperties;
import com.finder.global.security.jwt.dto.Jwt;
import com.finder.global.security.jwt.enums.JwtType;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    private final JwtProperties jwtProperties;
    private final UserDetailsService userDetailsService;
    private SecretKey key;

    @PostConstruct
    public void init() {
        key = new SecretKeySpec(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS512.key().build().getAlgorithm());
    }

    public String getEmail(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public JwtType getType(String token) {
        return JwtType.valueOf(Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getHeader()
                .getType()
        );
    }

    public Jwt generateToken(UserEntity user) {
        String accessToken = generateAccessToken(user);
        String refreshToken = generateRefreshToken(user);

        return new Jwt(accessToken, refreshToken);
    }

    public Authentication getAuthentication(String token) {
        String email = getEmail(token);
        UserDetails details = userDetailsService.loadUserByUsername(email);

        return new UsernamePasswordAuthenticationToken(details, "", details.getAuthorities());
    }

    public String extractToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }

        return null;
    }

    private String generateAccessToken(UserEntity user) {
        Date now = new Date();

        return Jwts.builder()
                .header()
                .type(JwtType.ACCESS.name())
                .and()
                .subject(user.getEmail())
                .signWith(key)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + jwtProperties.getAccessTokenExpiration()))
                .issuer(jwtProperties.getIssuer())
                .compact();
    }

    private String generateRefreshToken(UserEntity user) {
        Date now = new Date();

        return Jwts.builder()
                .header()
                .type(JwtType.REFRESH.name())
                .and()
                .subject(user.getEmail())
                .signWith(key)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + jwtProperties.getRefreshTokenExpiration()))
                .issuer(jwtProperties.getIssuer())
                .compact();
    }
}
