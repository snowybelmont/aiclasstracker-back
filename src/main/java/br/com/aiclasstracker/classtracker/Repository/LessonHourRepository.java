package br.com.aiclasstracker.classtracker.Repository;

import br.com.aiclasstracker.classtracker.Entity.LessonHourEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LessonHourRepository extends JpaRepository<LessonHourEntity, Long> {
    @Query("""
        SELECT lh
        FROM LessonHourEntity lh
        LEFT JOIN ClassLessonEntity cl ON cl.lessonHour.id = lh.id
        LEFT JOIN ClassStudentEntity cs ON cs.classField.id = cl.classField.id
        WHERE (cs.student.id = :userId OR lh.professor.id = :userId)
        AND lh.day = :day
        GROUP BY lh.id, lh.curse.id, lh.lesson.id, lh.professor.id, lh.room, lh.time, lh.day, lh.semester
        ORDER BY lh.semester, lh.curse.id, lh.time
    """)
    List<LessonHourEntity> findUserDailyLessons(Long userId, Long day);

    @Query("""
        SELECT lh
        FROM LessonHourEntity lh
        LEFT JOIN ClassLessonEntity cl ON cl.lessonHour.id = lh.id
        LEFT JOIN ClassStudentEntity cs ON cs.classField.id = cl.classField.id
        WHERE (cs.student.id = :userId OR lh.professor.id = :userId)
        GROUP BY lh.id, lh.curse.id, lh.lesson.id, lh.professor.id, lh.room, lh.time, lh.day, lh.semester
        ORDER BY lh.semester, lh.curse.id, lh.day, lh.time
    """)
    List<LessonHourEntity> findAllUserLessons(Long userId);
}
