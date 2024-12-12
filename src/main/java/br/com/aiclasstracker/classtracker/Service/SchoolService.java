package br.com.aiclasstracker.classtracker.Service;

import br.com.aiclasstracker.classtracker.Entity.*;
import br.com.aiclasstracker.classtracker.Exception.NoLessonFoundException;
import br.com.aiclasstracker.classtracker.Exception.NoStudentClassFoundException;
import br.com.aiclasstracker.classtracker.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.StringJoiner;

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

    public List<PresenceHistoryEntity> findPresencesByCall(CallHistoryEntity callHistory) {
        return presenceHistoryRepository.findAllByCallHistory(callHistory);
    }

    public CallHistoryEntity findCallByDate(Long lessonHourId) {
        return callHistoryRepository.findCallByDate(lessonHourId).orElse(null);
    }

    public LessonHourEntity findNowLessonForCall(UserEntity professor, Long day, String time) {
        return lessonHourRepository.findByProfessorAndDayAndTime(professor, day, time).orElse(null);
    }

    public CallHistoryEntity findCallByLessonHour(Long lessonHourId) {
        return callHistoryRepository.findCallByDateNow(lessonHourId).orElse(null);
    }

    public void makeCallInFatec(String lessonName, String hour, List<String> studentsRas) throws IOException {
        URL url = new URL("http://localhost:5000/makeCall");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");

        StringJoiner sj = new StringJoiner(",");
        for (String ra : studentsRas) {
            sj.add("\"" + ra + "\"");
        }

        String data = "{\"className\":\"" + lessonName + "\",\"hour\":\"" + hour + "\",\"studentsRas\":[" + sj.toString() + "]}";
        connection.setDoOutput(true);
        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(data.getBytes());
        outputStream.close();

        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();
        } else {
            System.out.println("Erro: " + responseCode);
        }
    }

    public Long getDayOfWeek() {
        LocalDateTime now = LocalDateTime.now();
        long dayOfWeek = now.getDayOfWeek().getValue();

        return dayOfWeek == 7 ? 1 : dayOfWeek + 1;
    }
}
