package br.com.aiclasstracker.classtracker.DTO;

import java.util.List;

public record ResizeImageRequestDTO(String imageBase64, List<FaceDTO> facesBounding) { }
