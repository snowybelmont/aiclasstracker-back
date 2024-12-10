package br.com.aiclasstracker.classtracker.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter @Setter
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_TOKEN_USER")
public class TokenEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id", unique = true, nullable = false, columnDefinition = "INT")
    private Long id;

    @Size(max = 276)
    @NonNull
    @Column(name = "user_email", nullable = false, length = 276)
    private String email;

    @Size(max = 600)
    @NonNull
    @Column(name= "access_token", nullable = false, length = 600)
    private String token;
}