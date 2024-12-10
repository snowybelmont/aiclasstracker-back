package br.com.aiclasstracker.classtracker.DTO;

public record DailyLessonsResponseDTO(String room, String time, Long day, String lessonAbr, Long semester, String curseAbr, String professorSurname) { }
