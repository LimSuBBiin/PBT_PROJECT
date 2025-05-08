package org.execute.config;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.execute.domain.Mem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
@RequiredArgsConstructor
@Service
public class TokenProvider {

    private final JwtProperties jwtProperties;
    private static final long ACCESS_TOKEN_EXPIRY = 1000 * 60 * 30; // 30분
    private static final long REFRESH_TOKEN_EXPIRY = 1000 * 60 * 60 * 24 * 7; // 7일
    private final Key key;
    // 🔹 Secret 문자열을 Key 객체로 변환 (HMAC SHA256 용)
//    private Key getSigningKey() {
//        byte[] keyBytes = jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
    @Autowired
    public TokenProvider(JwtProperties jwtProperties) {
        System.out.println("Secret Key 값: " + jwtProperties.getSecret()); // 디버깅용 로그 추가
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getSecret()));
        this.jwtProperties = jwtProperties;
    }

    public String createToken(Mem mem, long expiry) {
        return Jwts.builder()
                .claim("idx",mem.getMemMstIdx())
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setSubject(mem.getMemAct())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiry))
                .signWith(key, SignatureAlgorithm.HS256)
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
            return Jwts.parserBuilder().setSigningKey(key).build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            return null;
        }
    }
}
