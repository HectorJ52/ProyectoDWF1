package sv.edu.udb.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sv.edu.udb.model.Usuario;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    public String generateToken(Usuario usuario) {

        return Jwts.builder()
                .subject(usuario.getUsername())
                .claim("rol", usuario.getRol().getNombre().name())
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis() + jwtExpiration
                        )
                )
                .signWith(getSigningKey())
                .compact();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractRol(String token) {
        return extractAllClaims(token)
                .get("rol", String.class);
    }

    public boolean isTokenValid(
            String token,
            String username
    ) {

        String tokenUsername = extractUsername(token);

        return tokenUsername.equals(username)
                && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token)
                .before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractAllClaims(token)
                .getExpiration();
    }

    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {

        byte[] keyBytes =
                Decoders.BASE64.decode(secretKey);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}