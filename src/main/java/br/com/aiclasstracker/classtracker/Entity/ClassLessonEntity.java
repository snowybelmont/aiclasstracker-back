package br.com.aiclasstracker.classtracker.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_CLASS_LESSON")
public class ClassLessonEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 11L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_lesson_id", nullable = false, columnDefinition = "INT")
    private Long id;

    @NonNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "class_id", nullable = false)
    private ClassEntity classField;

    @NonNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "lesson_hour_id", nullable = false)
    private LessonHourEntity lessonHour;
}