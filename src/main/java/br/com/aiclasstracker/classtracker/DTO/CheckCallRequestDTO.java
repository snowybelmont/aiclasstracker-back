package br.com.aiclasstracker.classtracker.DTO;

import java.util.List;

public record CheckCallRequestDTO(List<DailyLessonsResponseDTO> dailyLessons) { }
