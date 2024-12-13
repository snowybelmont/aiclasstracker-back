package br.com.aiclasstracker.classtracker.Repository;

import br.com.aiclasstracker.classtracker.Entity.CallHistoryEntity;
import br.com.aiclasstracker.classtracker.Entity.ClassStudentEntity;
import br.com.aiclasstracker.classtracker.Entity.PresenceHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PresenceHistoryRepository extends JpaRepository<PresenceHistoryEntity, Long> {

    Optional<PresenceHistoryEntity> findByClassStudentAndCallHistory(ClassStudentEntity classStudent, CallHistoryEntity callHistory);
    List<PresenceHistoryEntity> findAllByCallHistory(CallHistoryEntity callHistory);

    @Query("""
        SELECT COUNT(1)
        FROM PresenceHistoryEntity ph
        WHERE ph.classStudent.student.id = :studentId
        AND ph.callHistory.id IN (
            SELECT ch.id
            FROM CallHistoryEntity ch
            INNER JOIN LessonHourEntity lh ON lh.id = ch.lessonHour.id
            WHERE lh.lesson.id = :lessonId
            AND lh.curse.id = :curseId
            AND lh.day = :day
            AND lh.semester = :semester
        )
    """)
    int findQtdPresenceToLesson(Long studentId, Long lessonId, Long curseId, Long day, Long semester);
}
