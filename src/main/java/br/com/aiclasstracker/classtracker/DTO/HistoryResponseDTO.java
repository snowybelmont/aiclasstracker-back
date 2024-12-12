package br.com.aiclasstracker.classtracker.DTO;

import java.time.LocalDateTime;

public record HistoryResponseDTO(LocalDateTime callDate, String lessonAbr, String lessonTime, int period, String classDesc, String curseAbr, Long ra, String userName, String userPhoto, boolean havePresence) { }
