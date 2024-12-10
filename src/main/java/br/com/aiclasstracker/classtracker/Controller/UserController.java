package br.com.aiclasstracker.classtracker.Controller;

import br.com.aiclasstracker.classtracker.DTO.UserDataResponseDTO;
import br.com.aiclasstracker.classtracker.Entity.UserEntity;
import br.com.aiclasstracker.classtracker.Exception.TokenNotFoundException;
import br.com.aiclasstracker.classtracker.Exception.UserNotFoundException;
import br.com.aiclasstracker.classtracker.Service.TokenService;
import br.com.aiclasstracker.classtracker.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final TokenService tokenService;

    @GetMapping("/data")
    public ResponseEntity<UserDataResponseDTO> getUserByAcessToken(@RequestHeader("Authorization") String accessToken) {
        try {
            String token = accessToken.replace("Bearer ", "");
            UserEntity user = userService.findUserByEmail(tokenService.findTokenByToken(token).getEmail());
            return ResponseEntity.ok(new UserDataResponseDTO(user.getRa(), user.getSurname(), user.getRole().getCode(), "Usuário encontrado"));
        } catch (TokenNotFoundException | UserNotFoundException e) {
            return ResponseEntity.status(404).body(new UserDataResponseDTO(null, null, null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new UserDataResponseDTO(null, null, null, e.getMessage()));
        }
    }
}
