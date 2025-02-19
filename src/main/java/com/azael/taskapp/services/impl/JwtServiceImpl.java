package com.azael.taskapp.services.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
// import org.springframework.security.core.userdetails.UserDetails;

import com.azael.taskapp.persistence.repositories.RevokedTokenRepository;
import com.azael.taskapp.services.JwtService;

// import com.azael.taskapp.persistence.entities.User;

@Component
@Slf4j
public class JwtServiceImpl implements JwtService {
    @Value("${api.security.token.secret}")
    private String SECRET_KEY;

    @Value("${api.security.token.expiration}")
    private Long JWT_EXPIRATION;

    private final RevokedTokenRepository revokedTokenRepository;
    
    public JwtServiceImpl(RevokedTokenRepository revokedTokenRepository) {
        this.revokedTokenRepository = revokedTokenRepository;
    }

    public String extractUsername(String token) {
        // log.info("token: "+extractClaim(token, Claims::getSubject));
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Map<String, String>  generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public Map<String, String>  generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, JWT_EXPIRATION);
    }

    public long getExpirationTime() {
        return JWT_EXPIRATION;
    }

    /**
     * Genera un token JWT con su fecha de expiración.
     *
     * @param user El usuario para el cual se genera el token.
     * @return Un mapa que contiene el token y su fecha de expiración.
     */
    private  Map<String, String> buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        // Calcular la fecha de expiración
        Date expirationDate = new Date(System.currentTimeMillis() + expiration);
        String token = Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expirationDate)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = formatter.format(expirationDate);
                 // Devolver el token y su fecha de expiración
                Map<String, String> tokenDetails = new HashMap<>();
                tokenDetails.put("token", token);
                tokenDetails.put("expiresAt", formattedDate);

            log.info("Token generated for the user: {}", userDetails.getUsername());
            return tokenDetails;
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        // final String username = extractUsername(token);
        // return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);

        final String username = extractUsername(token);
        if (revokedTokenRepository.existsByToken(token)) {
            return false; // Token está en la blacklist
        }
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // private Key getSignInKey() {
    //     if (SECRET_KEY == null || SECRET_KEY.isEmpty()) {
    //         throw new IllegalArgumentException("La clave secreta JWT no está configurada.");
    //     }
    //     byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
    //     return Keys.hmacShaKeyFor(keyBytes);
    // }

    private Key getSignInKey() {
        if (SECRET_KEY == null || SECRET_KEY.isEmpty()) {
            throw new IllegalArgumentException("The JWT secret key is not configured.");
        }
    
        // Decodificar la clave desde Base64
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
    
        // Validar la longitud de la clave
        if (keyBytes.length < 32) { // 32 bytes = 256 bits
            throw new IllegalArgumentException("The JWT secret key does not meet the minimum length of 256 bits. "+SECRET_KEY);
        }
    
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
