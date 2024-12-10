package br.com.aiclasstracker.classtracker.DTO;

public record FaultsResponseDTO(String lessonAbr, String lessonName, String classDesc, String curseAbr, String professorSurname, int totalCalls, int totalFalts, Double percentFalts, int maxFalts) { }
