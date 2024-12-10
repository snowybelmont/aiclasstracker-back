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
@Table(name = "TB_LESSON")
public class LessonEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 4L;

    @Id
    @NonNull
    @Column(name = "lesson_id", nullable = false, columnDefinition = "INT")
    private Long id;

    @Size(max = 256)
    @NonNull
    @Column(name = "lesson_name", nullable = false, length = 256)
    private String name;

    @Size(max = 50)
    @NonNull
    @Column(name = "lesson_abr", nullable = false, length = 50)
    private String abbreviation;

    @NonNull
    @Column(name = "lesson_hours", nullable = false, columnDefinition = "INT")
    private Long hours;

    @Size(max = 20)
    @NonNull
    @Column(name = "lesson_type_hours", nullable = false, length = 20)
    private String typeHours;

    @NonNull
    @Column(name="lesson_max_falts", nullable = false, columnDefinition = "INT")
    private int maxFalts;
}