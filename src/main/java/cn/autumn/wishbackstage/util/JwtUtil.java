package cn.autumn.wishbackstage.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.Map;

/**
 * @author Autumn
 * Created in 2022/12/30
 */
public final class JwtUtil {
    @Value("${crypto.secret}")
    private String secret;

    @Value("${crypto.expires}")
    private long expires;

    @Value("${crypto.header}")
    private String header;

    @Value("${crypto.cookie}")
    private String cookie;

    private final SignatureAlgorithm rs512 = SignatureAlgorithm.RS512;

    public String generateToken(String value) {
        return Jwts.builder()
                .setSubject(value)
                .setIssuedAt(getCurrDate())
                .setExpiration(getExpireDate())
                .signWith(rs512, secret)
                .compact();
    }

    public String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(getCurrDate())
                .setExpiration(getExpireDate())
                .signWith(rs512, secret)
                .compact();
    }



    public Claims getClaimsByToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    public Date getCurrDate() {
        return new Date(System.currentTimeMillis());
    }

    public Date getExpireDate() {
        return new Date(System.currentTimeMillis() + this.expires);
    }
}
