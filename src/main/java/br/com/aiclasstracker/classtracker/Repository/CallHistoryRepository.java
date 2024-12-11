package br.com.aiclasstracker.classtracker.Repository;

import br.com.aiclasstracker.classtracker.Entity.CallHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CallHistoryRepository extends JpaRepository<CallHistoryEntity, Long> {
    @Query("""
        SELECT ch
        FROM CallHistoryEntity ch
        WHERE ch.lessonHour.professor.id = :professorId
        GROUP BY ch.id, ch.callDate, ch.lessonHour.id
    """)
    List<CallHistoryEntity> findAllProfessorCalls(Long professorId);

    @Query("""
        SELECT ch
        FROM CallHistoryEntity ch
        INNER JOIN ClassLessonEntity cl ON cl.lessonHour.id = ch.lessonHour.id
        INNER JOIN ClassStudentEntity cs ON cs.classField.id = cl.classField.id
        WHERE cs.student.id = :studentId
        GROUP BY ch.id, ch.callDate, ch.lessonHour.id
    """)
    List<CallHistoryEntity> findAllCallsToStudent(Long studentId);

    @Query("""
        SELECT COUNT(1)
        FROM CallHistoryEntity ch
        INNER JOIN LessonHourEntity lh ON lh.id = ch.lessonHour.id
        WHERE lh.lesson.id = :lessonId
        AND lh.curse.id = :curseId
        AND lh.day = :day
        AND lh.semester = :semester
    """)
    int findQtdCallsToLesson(Long lessonId, Long curseId, Long day, Long semester);

    @Query("""
        SELECT ch
        FROM CallHistoryEntity ch
        INNER JOIN LessonHourEntity lh ON lh.id = ch.id
        WHERE lh.professor.id = :professorId
        AND lh.lesson.abbreviation = :lessonAbr
        AND lh.day = :day
        AND TRIM(lh.time) LIKE TRIM(:time)
        AND lh.semester = :semester
    """)
    Optional<CallHistoryEntity> findCallHistoryExists(Long professorId, String lessonAbr, Long day, String time, Long semester);
}
