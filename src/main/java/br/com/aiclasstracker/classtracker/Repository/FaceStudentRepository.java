package br.com.aiclasstracker.classtracker.Repository;

import br.com.aiclasstracker.classtracker.Entity.FaceStudentEntity;
import br.com.aiclasstracker.classtracker.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FaceStudentRepository extends JpaRepository<FaceStudentEntity, Long> {
    Optional<FaceStudentEntity> findByStudent(UserEntity student);
}
