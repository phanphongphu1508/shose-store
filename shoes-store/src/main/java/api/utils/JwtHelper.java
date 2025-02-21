package api.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Component
public class JwtHelper {
    @Value("${jwt.key}")
    private String keyJWT;

    public boolean decryptToken(String token) {
        boolean result = false;
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(keyJWT));
        try {
            Jwts.parser().verifyWith(key).build().parseClaimsJws(token);
            result = true;
        } catch (Exception e) {
            System.out.println("Decrypt token failed" + e.getMessage());
        }


        return result;
    }
}
