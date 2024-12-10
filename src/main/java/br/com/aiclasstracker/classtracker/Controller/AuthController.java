package br.com.aiclasstracker.classtracker.Controller;

import br.com.aiclasstracker.classtracker.DTO.*;
import br.com.aiclasstracker.classtracker.Entity.*;
import br.com.aiclasstracker.classtracker.Exception.*;
import br.com.aiclasstracker.classtracker.Service.TokenService;
import br.com.aiclasstracker.classtracker.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        try {
            UserEntity userEntity = userService.findUserByEmail(request.email());

            if (!userService.isPasswordCorrect(request.password(), userEntity.getPassword())) {
                throw new PasswordIncorretException();
            }

            TokenEntity tokenEntity = tokenService.findTokenByEmail(userEntity.getEmail());
            if (tokenEntity == null || tokenService.verifyTokenExpired(tokenEntity.getToken(), Instant.now())) {
                tokenEntity = tokenService.getToken(userEntity.getEmail(), userEntity.getRole().getSecurityRole(), tokenEntity);
            }

            return ResponseEntity.ok(new LoginResponseDTO(tokenEntity.getToken(), "Login efetuado"));
        } catch (UserNotFoundException | PasswordIncorretException e) {
            return ResponseEntity.status(403).body(new LoginResponseDTO(null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new LoginResponseDTO(null, e.getMessage()));
        }
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<RefreshTokenResponseDTO> refreshToken(@RequestBody RefreshTokenRequestDTO request) {
        try {
            TokenEntity tokenEntity = tokenService.findTokenByToken(request.accessToken());
            UserEntity userEntity = userService.findUserByEmail(tokenEntity.getEmail());

            if (!tokenService.verifyTokenExpired(tokenEntity.getToken(), Instant.now())) {
                return ResponseEntity.ok(new RefreshTokenResponseDTO(tokenEntity.getToken(), "Token recuperado"));
            }

            tokenEntity = tokenService.getToken(userEntity.getEmail(), userEntity.getRole().getSecurityRole(), tokenEntity);
            return ResponseEntity.ok(new RefreshTokenResponseDTO(tokenEntity.getToken(), "Token atualizado"));
        } catch (TokenNotFoundException | UserNotFoundException e) {
            return ResponseEntity.status(403).body(new RefreshTokenResponseDTO(null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new RefreshTokenResponseDTO(null, e.getMessage()));
        }
    }

    @PostMapping("/checkToken")
    public ResponseEntity<CheckTokenResponseDTO> checkToken(@RequestHeader("Authorization") String accessToken) {
        try {
            String token = accessToken.replace("Bearer ", "");
            return ResponseEntity.ok(new CheckTokenResponseDTO(tokenService.findTokenByToken(token) != null, "Token existe"));
        } catch (TokenNotFoundException e) {
            return ResponseEntity.status(404).body(new CheckTokenResponseDTO(false, e.getMessage()));
        }
    }
}