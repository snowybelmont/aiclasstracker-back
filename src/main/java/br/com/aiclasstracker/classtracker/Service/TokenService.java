package br.com.aiclasstracker.classtracker.Service;

import br.com.aiclasstracker.classtracker.Config.Security.SecurityConfig;
import br.com.aiclasstracker.classtracker.Entity.TokenEntity;
import br.com.aiclasstracker.classtracker.Exception.*;
import br.com.aiclasstracker.classtracker.Repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;

@RequiredArgsConstructor
@Service
public class TokenService {
    private final SecurityConfig securityConfig;
    private final TokenRepository tokenRepository;

    public TokenEntity findTokenByToken(String token) {
        return tokenRepository.findByToken(token).orElseThrow(TokenNotFoundException::new);
    }

    public TokenEntity findTokenByEmail(String email) {
        return tokenRepository.findByEmail(email).orElse(null);
    }

    private Instant getExpirationToken(String token) {
        try {
            return securityConfig.jwtDecoder().decode(token).getExpiresAt();
        } catch (Exception e) {
            return Instant.parse(e.getMessage().split("Jwt expired at ")[1]);
        }
    }

    public boolean verifyTokenExpired(String token, Instant now) {
        return getExpirationToken(token).isBefore(now);
    }

    private String generateToken(String userEmail, String userSecurityRole) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("classtracker-auth")
                .subject(userEmail)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(60L))
                .claim("scope", userSecurityRole)
                .build();
        String token = securityConfig.jwtEncoder().encode(JwtEncoderParameters.from(claims)).getTokenValue();

        if (token == null || !token.startsWith("ey")) {
            throw new TokenGenerateException();
        }

        return token;
    }

    private TokenEntity saveToken(String token, String userEmail, TokenEntity tokenEntity) {
        if (tokenEntity == null) {
            tokenEntity = new TokenEntity(userEmail, token);
        } else {
            tokenEntity.setToken(token);
        }
        return tokenRepository.save(tokenEntity);
    }

    public TokenEntity getToken(String userEmail, String userSecurityRole, TokenEntity tokenEntity) {
        return saveToken(generateToken(userEmail, userSecurityRole), userEmail, tokenEntity);
    }
}