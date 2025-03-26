package org.execute.config;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Value;
import org.execute.domain.Mem;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class TokenProvider {

    private final int expiratxion;
    private static final long ACCESS_TOKEN_EXPIRY = 1000 * 60 * 30; // 30분
    private static final long REFRESH_TOKEN_EXPIRY = 1000 * 60 * 60 * 24 * 7; // 7일
    private final Key secretKey;

    // @Value를 사용해 application.yml에서 값 주입
    public TokenProvider(JwtProperties jwtProperties) {
        this.expiratxion = jwtProperties.getExpiration();
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    }

    public String createToken(Mem mem, long expiry) {
        return Jwts.builder()
                .claim("idx",mem.getMemMstIdx())
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setSubject(mem.getMemAct())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiry))
                .signWith(secretKey)
                .compact();
    }

    public String createAccessToken(Mem mem) {
        return createToken(mem, ACCESS_TOKEN_EXPIRY);
    }

    public String createRefreshToken(Mem mem) {
        return createToken(mem, REFRESH_TOKEN_EXPIRY);
    }

    public String validateToken(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(secretKey).build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            return null;
        }
    }
}
