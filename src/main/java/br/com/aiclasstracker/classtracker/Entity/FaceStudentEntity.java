package br.com.aiclasstracker.classtracker.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_FACE_STUDENT")
public class FaceStudentEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 7L;

    @Id
    @NonNull
    @Size(max = 50)
    @Column(name = "face_id", nullable = false, length = 50)
    private String faceId;

    @NonNull
    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private UserEntity student;

    @NonNull
    @Column(name = "base64", nullable = false, columnDefinition = "LONGTEXT")
    private String base64;
}