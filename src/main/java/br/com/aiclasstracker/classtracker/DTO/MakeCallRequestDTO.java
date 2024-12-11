package br.com.aiclasstracker.classtracker.DTO;

import java.util.List;

public record MakeCallRequestDTO(String lessonAbr, String curseAbr, Long day, String time, Long semester, List<String> studentsRa) { }
