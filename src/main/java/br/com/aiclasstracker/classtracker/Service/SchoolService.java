package br.com.aiclasstracker.classtracker.Service;

import br.com.aiclasstracker.classtracker.Entity.*;
import br.com.aiclasstracker.classtracker.Exception.NoLessonFoundException;
import br.com.aiclasstracker.classtracker.Exception.NoStudentClassFoundException;
import br.com.aiclasstracker.classtracker.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SchoolService {
    private final LessonHourRepository lessonHourRepository;
    private final CallHistoryRepository callHistoryRepository;
    private final UserRepository userRepository;
    private final ClassStudentsRepository classStudentsRepository;
    private final FaceStudentRepository faceStudentRepository;
    private final PresenceHistoryRepository presenceHistoryRepository;

    public List<LessonHourEntity> findUserDailyLessons(Long userId, Long day) {
        return lessonHourRepository.findUserDailyLessons(userId, day);
    }

    public List<CallHistoryEntity> findCallsToProfessor(Long professorId) {
        return callHistoryRepository.findAllProfessorCalls(professorId);
    }

    public List<UserEntity> findStudentsInLesson(Long lessonHourId) {
        return userRepository.findStudentsByLessonHour(lessonHourId);
    }

    public ClassStudentEntity findStudentClass(Long studentId, Long lessonHourId) {
        return classStudentsRepository.findStudentClass(studentId, lessonHourId).orElse(null);
    }

    public FaceStudentEntity findStudentPhoto(UserEntity student) {
        return faceStudentRepository.findByStudent(student).orElse(null);
    }

    public PresenceHistoryEntity findStudentPresenceInCall(ClassStudentEntity classStudent, CallHistoryEntity callHistory) {
        return presenceHistoryRepository.findByClassStudentAndCallHistory(classStudent, callHistory).orElse(null);
    }

    public List<CallHistoryEntity> findCallsToStudent(Long studentId) {
        return callHistoryRepository.findAllCallsToStudent(studentId);
    }

    public List<LessonHourEntity> findLessonsToStudent(Long studentId) {
        return lessonHourRepository.findAllUserLessons(studentId);
    }

    public int findQtdCallsToLesson(Long lessonId, Long curseId, Long day, Long semester) {
        return callHistoryRepository.findQtdCallsToLesson(lessonId, curseId, day, semester);
    }

    public int findQtdPresenceToLesson(Long studentId, Long lessonId, Long curseId, Long day, Long semester) {
        return presenceHistoryRepository.findQtdPresenceToLesson(studentId, lessonId, curseId, day, semester);
    }

    public CallHistoryEntity findCallHistoryExists(Long professorId, String lessonAbr, Long day, String time, Long semester) {
        return callHistoryRepository.findCallHistoryExists(professorId, lessonAbr, day, time, semester).orElse(null);
    }

    public LessonHourEntity findNowLessonForProfessor(Long professorId, String lessonAbr, Long day, String time, Long semester) {
        return lessonHourRepository.findProfessorLessonNow(professorId, lessonAbr, day, time, semester).orElseThrow(NoLessonFoundException::new);
    }

    public CallHistoryEntity saveCall(CallHistoryEntity callHistory) {
        return callHistoryRepository.save(callHistory);
    }

    public ClassStudentEntity findClassStudentForCall(Long studentRa, String curseAbr, String semester) {
        return classStudentsRepository.findClassStudentForCall(studentRa, curseAbr, semester).orElse(null);
    }

    public List<PresenceHistoryEntity> savePresences(List<PresenceHistoryEntity> presenceHistoryList) {
        return presenceHistoryRepository.saveAll(presenceHistoryList);
    }

    public Long getDayOfWeek() {
        LocalDateTime now = LocalDateTime.now();
        long dayOfWeek = now.getDayOfWeek().getValue();

        return dayOfWeek == 7 ? 1 : dayOfWeek + 2;
    }
}
