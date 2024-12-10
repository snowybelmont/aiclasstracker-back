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
@Table(name = "TB_CLASS")
public class ClassEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 8L;

    @Id
    @NonNull
    @Column(name = "class_id", nullable = false, columnDefinition = "INT")
    private Long id;

    @Size(max = 100)
    @NonNull
    @Column(name = "class_description", nullable = false, length = 100)
    private String classDesc;

    @NonNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "curse_id", nullable = false)
    private CurseEntity curse;

    @Size(max = 20)
    @NonNull
    @Column(name = "class_shift", nullable = false, length = 20)
    private String classShift;
}