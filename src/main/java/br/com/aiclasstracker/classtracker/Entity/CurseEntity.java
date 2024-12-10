package br.com.aiclasstracker.classtracker.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_CURSE")
public class CurseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 3L;

    @Id
    @NonNull
    @Column(name = "curse_id", nullable = false, columnDefinition = "INT")
    private Long id;

    @Size(max = 256)
    @NonNull
    @Column(name = "curse_name", nullable = false, length = 256)
    private String name;

    @Size(max = 10)
    @NonNull
    @Column(name = "curse_abr", nullable = false, length = 10)
    private String abbreviation;
}