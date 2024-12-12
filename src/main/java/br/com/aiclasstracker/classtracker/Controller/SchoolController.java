package br.com.aiclasstracker.classtracker.Controller;

import br.com.aiclasstracker.classtracker.DTO.*;
import br.com.aiclasstracker.classtracker.Entity.*;
import br.com.aiclasstracker.classtracker.Exception.*;
import br.com.aiclasstracker.classtracker.Service.SchoolService;
import br.com.aiclasstracker.classtracker.Service.UserService;
import br.com.aiclasstracker.classtracker.Utils.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/school")
public class SchoolController {
    private final SchoolService schoolService;
    private final UserService userService;

    @PreAuthorize("hasAuthority('SCOPE_ROLE_STUDENT') or hasAuthority('SCOPE_ROLE_PROFESSOR')")
    @GetMapping("/users/{ra}/dailyLessons")
    public ResponseEntity<List<DailyLessonsResponseDTO>> getDailyLessons(@RequestHeader("Authorization") String accessToken, @PathVariable("ra") Long ra) {
        try {
            UserEntity userEntity = userService.checkUserIsRequester(ra, accessToken);
            Long dayOfWeek = schoolService.getDayOfWeek();
            List<LessonHourEntity> lessonsHourFound = schoolService.findUserDailyLessons(userEntity.getId(), dayOfWeek);

            if(lessonsHourFound.isEmpty()) {
                throw new UserNoLessonException();
            }

            return ResponseEntity.ok(lessonsHourFound.stream()
                    .map(lessonHour -> new DailyLessonsResponseDTO(
                            lessonHour.getRoom(),
                            lessonHour.getTime(),
                            lessonHour.getDay(),
                            lessonHour.getLesson().getAbbreviation(),
                            lessonHour.getSemester(),
                            lessonHour.getCurse().getAbbreviation(),
                            lessonHour.getProfessor().getSurname()
                    )).toList());
        }catch (UserNotFoundException | UserNoLessonException e) {
            return ResponseEntity.status(404).body(null);
        } catch (UserNoPermissionException e) {
            return ResponseEntity.status(403).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_STUDENT') or hasAuthority('SCOPE_ROLE_PROFESSOR')")
    @GetMapping("/users/{ra}/history")
    public ResponseEntity<List<HistoryResponseDTO>> getHistory(@RequestHeader("Authorization") String accessToken, @PathVariable("ra") Long ra) {
        try {
            UserEntity userEntity = userService.checkUserIsRequester(ra, accessToken);
            List<CallHistoryEntity> callsHistoryFound = userEntity.getRole().equals(RoleEnum.PROFESSOR)
                            ? schoolService.findCallsToProfessor(userEntity.getId())
                            : schoolService.findCallsToStudent(userEntity.getId());

            if(callsHistoryFound.isEmpty()) {
                throw new NoCallHistoryFoundException();
            }

            List<HistoryResponseDTO> historyResponseList = new ArrayList<>();
            for(CallHistoryEntity callHistory : callsHistoryFound) {
                if(userEntity.getRole().equals(RoleEnum.PROFESSOR)) {
                    List<UserEntity> studentsFound = schoolService.findStudentsInLesson(callHistory.getLessonHour().getId());

                    for (UserEntity student : studentsFound) {
                        ClassStudentEntity classStudent = schoolService.findStudentClass(student.getId(), callHistory.getLessonHour().getId());

                        if (classStudent == null) {
                            continue;
                        }

                        FaceStudentEntity faceStudent = schoolService.findStudentPhoto(student);
                        PresenceHistoryEntity presenceHistory = schoolService.findStudentPresenceInCall(classStudent, callHistory);

                        historyResponseList.add(
                                new HistoryResponseDTO(
                                        callHistory.getCallDate(),
                                        callHistory.getLessonHour().getLesson().getAbbreviation(),
                                        classStudent.getClassField().getClassDesc(),
                                        classStudent.getClassField().getCurse().getAbbreviation(),
                                        student.getRa(),
                                        student.getFullName(),
                                        faceStudent != null ? faceStudent.getBase64() : null,
                                        presenceHistory != null
                                )
                        );
                    }
                } else {
                    ClassStudentEntity classStudent = schoolService.findStudentClass(userEntity.getId(), callHistory.getLessonHour().getId());

                    if(classStudent != null) {
                        PresenceHistoryEntity presenceHistory = schoolService.findStudentPresenceInCall(classStudent, callHistory);

                        historyResponseList.add(
                                new HistoryResponseDTO(
                                        callHistory.getCallDate(),
                                        callHistory.getLessonHour().getLesson().getAbbreviation(),
                                        classStudent.getClassField().getClassDesc(),
                                        classStudent.getClassField().getCurse().getAbbreviation(),
                                        null,
                                        callHistory.getLessonHour().getProfessor().getSurname(),
                                        null,
                                        presenceHistory != null
                                )
                        );
                    }
                }
            }

            return ResponseEntity.ok(historyResponseList);
        } catch (UserNotFoundException | NoCallHistoryFoundException e) {
            return ResponseEntity.status(404).body(null);
        } catch (UserNoPermissionException e) {
            return ResponseEntity.status(403).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_STUDENT')")
    @GetMapping("/users/{ra}/faults")
    public ResponseEntity<List<FaultsResponseDTO>> getFaultsStats(@RequestHeader("Authorization") String accessToken, @PathVariable("ra") Long ra) {
        try {
            UserEntity userEntity = userService.checkUserIsRequester(ra, accessToken);
            List<LessonHourEntity> lessonsHourFound = schoolService.findLessonsToStudent(userEntity.getId());

            if (lessonsHourFound.isEmpty()) {
                throw new NoLessonFoundException();
            }

            List<FaultsResponseDTO> faultsResponseList = new ArrayList<>();
            for (LessonHourEntity lessonHour : lessonsHourFound) {
                boolean alredyExits = false;
                for(FaultsResponseDTO faultsResponse : faultsResponseList) {
                    if(faultsResponse.lessonAbr().equals(lessonHour.getLesson().getAbbreviation())) {
                        alredyExits = true;
                        break;
                    }
                }

                if(alredyExits) {
                    continue;
                }

                ClassStudentEntity classStudent = schoolService.findStudentClass(userEntity.getId(), lessonHour.getId());

                if(classStudent != null) {
                    int qtdCalls = schoolService.findQtdCallsToLesson(lessonHour.getLesson().getId(), lessonHour.getCurse().getId(), lessonHour.getDay(), lessonHour.getSemester());
                    int qtdPresence = schoolService.findQtdPresenceToLesson(userEntity.getId(), lessonHour.getLesson().getId(), lessonHour.getCurse().getId(), lessonHour.getDay(), lessonHour.getSemester());

                    faultsResponseList.add(
                        new FaultsResponseDTO(
                            lessonHour.getLesson().getAbbreviation(),
                            lessonHour.getLesson().getName(),
                            classStudent.getClassField().getClassDesc(),
                            classStudent.getClassField().getCurse().getAbbreviation(),
                            lessonHour.getProfessor().getSurname(),
                            qtdCalls,
                            qtdCalls - qtdPresence,
                            qtdCalls == 0 ? 0.0 : qtdPresence == 0 ? 100.0 : ((double) (qtdCalls - qtdPresence) / lessonHour.getLesson().getMaxFalts()) * 100,
                            lessonHour.getLesson().getMaxFalts()
                        )
                    );
                }
            }

            return ResponseEntity.ok(faultsResponseList);
        } catch (UserNotFoundException | NoCallHistoryFoundException e) {
            return ResponseEntity.status(404).body(null);
        } catch (UserNoPermissionException e) {
            return ResponseEntity.status(403).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_PROFESSOR')")
    @PostMapping("/users/{ra}/makeCall")
    public ResponseEntity<List<UserDTO>> makeLessonCall(@RequestHeader("Authorization") String accessToken, @PathVariable("ra") Long ra, @RequestBody MakeCallRequestDTO request) {
        try {
            UserEntity userEntity = userService.checkUserIsRequester(ra, accessToken);
            CallHistoryEntity callHistoryFound = schoolService.findCallHistoryExists(userEntity.getId(), request.lessonAbr(), request.day(), request.time(), request.semester());

            if(callHistoryFound == null) {
                LessonHourEntity lessonHour = schoolService.findNowLessonForProfessor(userEntity.getId(), request.lessonAbr(), request.day(), request.time(), request.semester());
                callHistoryFound = new CallHistoryEntity(LocalDateTime.now(), lessonHour);
                CallHistoryEntity callHistorySaved = schoolService.saveCall(callHistoryFound);

                if(callHistorySaved == null) {
                    throw new CallSaveException();
                }
            }

            List<PresenceHistoryEntity> presencesHistoryList = new ArrayList<>();
            for(String studentRa : request.studentsRa()) {
                ClassStudentEntity classStudent = schoolService.findClassStudentForCall(Long.parseLong(studentRa), request.curseAbr(), String.valueOf(request.semester()));

                if(classStudent == null) {
                    continue;
                }

                presencesHistoryList.add(new PresenceHistoryEntity(classStudent, callHistoryFound));
            }

            List<UserDTO> studentsSaved = new ArrayList<>();
            if(!presencesHistoryList.isEmpty()) {
                List<PresenceHistoryEntity> presenceHistorySaved = schoolService.savePresences(presencesHistoryList);

                if(presenceHistorySaved.isEmpty()) {
                    throw new PresencesSaveException();
                }

                for(PresenceHistoryEntity presenceHistory : presenceHistorySaved) {
                    studentsSaved.add(new UserDTO(presenceHistory.getClassStudent().getStudent().getRa(), presenceHistory.getClassStudent().getStudent().getFullName()));
                }
            }

            return ResponseEntity.ok(studentsSaved);
        } catch (UserNotFoundException | NoLessonFoundException | NoStudentClassFoundException e) {
            return ResponseEntity.status(404).body(null);
        } catch (UserNoPermissionException e) {
            return ResponseEntity.status(403).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
