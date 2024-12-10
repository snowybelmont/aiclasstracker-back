package br.com.aiclasstracker.classtracker.Service;

import br.com.aiclasstracker.classtracker.DTO.FaceDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ImageService {
    public BufferedImage decodeBase64ToImage(String base64String) throws IOException {
        String base64 = base64String.replaceFirst("^data:image/[^;]+;base64,", "");

        byte[] imageBytes = Base64.getDecoder().decode(base64);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
        return ImageIO.read(byteArrayInputStream);
    }

    public String encodeImageToBase64(BufferedImage image, String imageFormat) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        ImageIO.write(image, imageFormat, byteArrayOutputStream);

        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        return Base64.getEncoder().encodeToString(imageBytes);
    }

    public BufferedImage cropImage(BufferedImage originalImage, Double x, Double y, Double width, Double height, List<FaceDTO> faces) {
        int imageWidth = originalImage.getWidth();
        int imageHeight = originalImage.getHeight();

        int xInt = (int) Math.round(x * imageWidth) - 40;
        int yInt = (int) Math.round(y * imageHeight) - 20;
        int widthInt = (int) Math.round(width * imageWidth);
        int heightInt = (int) Math.round(height * imageHeight);

        FaceDTO currentBoundingBox = new FaceDTO(height, x, y, width);
        FaceDTO nextBoundingBox = null;
        double minDistance = Double.MAX_VALUE;
        for (FaceDTO nextFace : faces) {
            if (!nextFace.equals(currentBoundingBox)) {
                double distance = calculateDistance(currentBoundingBox, nextFace);
                if (distance < minDistance) {
                    minDistance = distance;
                    nextBoundingBox = nextFace;
                }
            }
        }

        if (nextBoundingBox != null) {
            int expansionWidth = (int) (minDistance / 2);
            int expansionHeight = (int) (minDistance / 2);

            widthInt += expansionWidth * 2;
            heightInt += expansionHeight * 2;
        } else {
            widthInt *= 2;
            heightInt *= 2;
        }

        BufferedImage croppedImage = new BufferedImage(widthInt, heightInt, BufferedImage.TYPE_INT_RGB);

        AffineTransform transform = new AffineTransform();
        transform.translate(-xInt, -yInt);

        Graphics2D g2d = croppedImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.drawImage(originalImage, transform, null);
        g2d.dispose();

        BufferedImage resizedImage = new BufferedImage(widthInt, heightInt, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2dResized = resizedImage.createGraphics();
        g2dResized.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2dResized.drawImage(croppedImage, 0, 0, widthInt, heightInt, null);
        g2dResized.dispose();

        return resizedImage;
    }

    private double calculateDistance(FaceDTO rect1, FaceDTO rect2) {
        double x1 = rect1.Left() + rect1.Width() / 2;
        double y1 = rect1.Top() + rect1.Height() / 2;
        double x2 = rect2.Left() + rect2.Width() / 2;
        double y2 = rect2.Top() + rect2.Height() / 2;

        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
}
