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
@Table(name = "TB_LESSON_HOUR")
public class LessonHourEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 5L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lesson_hour_id", nullable = false, columnDefinition = "INT")
    private Long id;

    @NonNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "curse_id", nullable = false)
    private CurseEntity curse;

    @NonNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "lesson_id", nullable = false)
    private LessonEntity lesson;

    @NonNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "professor_id", nullable = false)
    private UserEntity professor;

    @Size(max = 10)
    @NonNull
    @Column(name = "class_room", nullable = false, length = 10)
    private String room;

    @Size(max = 20)
    @NonNull
    @Column(name = "class_time", nullable = false, length = 20)
    private String time;

    @NonNull
    @Column(name = "class_day", nullable = false, columnDefinition = "INT")
    private Long day;

    @NonNull
    @Column(name = "semester", nullable = false, columnDefinition = "INT")
    private Long semester;
}