package br.com.aiclasstracker.classtracker.Controller;

import br.com.aiclasstracker.classtracker.DTO.FaceDTO;
import br.com.aiclasstracker.classtracker.DTO.ResizeImageRequestDTO;
import br.com.aiclasstracker.classtracker.Service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/image")
public class ImageController {
    private final ImageService imageService;

    @PreAuthorize("hasAuthority('SCOPE_ROLE_PROFESSOR')")
    @PostMapping("/resize")
    public ResponseEntity<List<String>> resizeImage(@RequestBody ResizeImageRequestDTO request) {
        try {
            List<String> listBase64Resizeds = new ArrayList<>();

            for (FaceDTO face : request.faces()) {
                BufferedImage image = imageService.decodeBase64ToImage(request.photoBase64());
                BufferedImage croppedImage = imageService.cropImage(image, face.Left(), face.Top(), face.Width(), face.Height(), request.faces());
                String base64String = imageService.encodeImageToBase64(croppedImage, "JPEG");

                listBase64Resizeds.add(base64String);
            }

            return ResponseEntity.ok(listBase64Resizeds);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
