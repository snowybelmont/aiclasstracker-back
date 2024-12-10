package br.com.aiclasstracker.classtracker.Repository;

import br.com.aiclasstracker.classtracker.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByRa(Long ra);

    @Query("""
        SELECT u
        FROM UserEntity u
        INNER JOIN ClassStudentEntity cs ON cs.student.id = u.id
        INNER JOIN ClassLessonEntity cl ON cs.classField.id = cl.classField.id
        WHERE u.role = 'A'
        AND cl.lessonHour.id = :lessonHourId
    """)
    List<UserEntity> findStudentsByLessonHour(Long lessonHourId);
}