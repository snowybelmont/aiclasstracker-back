package br.com.aiclasstracker.classtracker.Repository;

import br.com.aiclasstracker.classtracker.Entity.ClassStudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ClassStudentsRepository extends JpaRepository<ClassStudentEntity, Long> {
    @Query("""
        SELECT cs
        FROM ClassStudentEntity cs
        INNER JOIN ClassLessonEntity cl ON cs.classField.id = cl.classField.id
        WHERE cs.student.id = :studentId
        AND cl.lessonHour.id = :lessonHourId
        GROUP BY cs.id, cs.classField.id, cs.student.id
    """)
    Optional<ClassStudentEntity> findStudentClass(Long studentId, Long lessonHourId);

    @Query("""
        SELECT cs
        FROM ClassStudentEntity cs
        WHERE cs.student.ra = :studentRa
        AND cs.classField.curse.abbreviation = :curseAbr
        AND SUBSTR(cs.classField.classDesc, 1, 1) = :semester
    """)
    Optional<ClassStudentEntity> findClassStudentForCall(Long studentRa, String curseAbr, String semester);
}
