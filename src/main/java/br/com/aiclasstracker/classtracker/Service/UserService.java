package br.com.aiclasstracker.classtracker.Service;

import br.com.aiclasstracker.classtracker.Entity.UserEntity;
import br.com.aiclasstracker.classtracker.Exception.UserNoPermissionException;
import br.com.aiclasstracker.classtracker.Exception.UserNotFoundException;
import br.com.aiclasstracker.classtracker.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final TokenService tokenService;

    public UserEntity findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }

    public UserEntity findUserByRa(Long ra) {
        return userRepository.findByRa(ra).orElseThrow(UserNotFoundException::new);
    }

    public boolean isPasswordCorrect(String passwordRequest, String passwordDatabase) {
        return passwordEncoder.matches(passwordRequest, passwordDatabase);
    }

    public UserEntity checkUserIsRequester(Long ra, String accessToken) {
        UserEntity userFound = findUserByRa(ra);
        UserEntity userRequested = findUserByEmail(tokenService.findTokenByToken(accessToken.replace("Bearer ", "")).getEmail());

        if (!Objects.equals(userRequested.getId(), userFound.getId())) {
            throw new UserNoPermissionException();
        }

        return userFound;
    }
}