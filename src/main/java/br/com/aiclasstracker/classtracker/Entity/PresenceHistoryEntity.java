package br.com.aiclasstracker.classtracker.Entity;

import jakarta.persistence.*;
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
@Table(name = "TB_PRESENCE_HISTORY")
public class PresenceHistoryEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 12L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "presence_id", nullable = false, columnDefinition = "INT")
    private Long id;

    @NonNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "class_student_id", nullable = false)
    private ClassStudentEntity classStudent;

    @NonNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "call_id", nullable = false)
    private CallHistoryEntity callHistory;
}