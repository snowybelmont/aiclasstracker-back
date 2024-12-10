package br.com.aiclasstracker.classtracker.Entity;

import br.com.aiclasstracker.classtracker.Utils.EnumConvert;
import br.com.aiclasstracker.classtracker.Utils.RoleEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_USER")
public class UserEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique = true, nullable = false, columnDefinition = "INT")
    private Long id;

    @NonNull
    @Column(name = "user_ra", unique = true, nullable = false, columnDefinition = "INT")
    private Long ra;

    @Size(max = 100)
    @NonNull
    @Column(name = "user_full_name", nullable = false, length = 100)
    private String fullName;

    @Size(max = 50)
    @NonNull
    @Column(name = "user_surname", nullable = false, length = 50)
    private String surname;

    @NonNull
    @Column(name = "user_role", nullable = false, length = 1, columnDefinition = "ENUM")
    @Convert(converter = EnumConvert.class)
    private RoleEnum role;

    @Size(max = 276)
    @NonNull
    @Column(name = "user_email", unique = true, nullable = false, length = 276)
    private String email;

    @Size(max = 256)
    @NonNull
    @Column(name = "user_password", nullable = false, length = 256)
    private String password;
}