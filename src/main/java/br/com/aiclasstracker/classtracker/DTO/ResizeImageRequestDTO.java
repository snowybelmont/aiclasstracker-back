package br.com.aiclasstracker.classtracker.DTO;

import java.util.List;

public record ResizeImageRequestDTO(String photoBase64, List<FaceDTO> faces) { }
