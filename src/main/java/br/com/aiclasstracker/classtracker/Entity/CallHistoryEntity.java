package br.com.aiclasstracker.classtracker.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_CALL_HISTORY")
public class CallHistoryEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 13L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "call_id", nullable = false, columnDefinition = "INT")
    private Long id;

    @NonNull
    @Column(name = "call_date", nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime callDate;

    @NonNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "lesson_hour_id", nullable = false)
    private LessonHourEntity lessonHour;
}