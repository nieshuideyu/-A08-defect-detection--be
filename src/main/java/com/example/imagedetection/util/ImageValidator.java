package com.example.imagedetection.util;

import com.example.imagedetection.exception.ImageProcessingException;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ImageValidator {
    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList(
        "image/jpeg",
        "image/png",
        "image/gif"
    );

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final int MIN_WIDTH = 100;
    private static final int MIN_HEIGHT = 100;

    public static void validateImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new ImageProcessingException("上传的文件为空");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new ImageProcessingException("文件大小超过限制（最大10MB）");
        }

        if (!ALLOWED_CONTENT_TYPES.contains(file.getContentType())) {
            throw new ImageProcessingException("不支持的文件类型，仅支持JPEG、PNG和GIF格式");
        }

        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                throw new ImageProcessingException("无效的图片文件");
            }

            if (image.getWidth() < MIN_WIDTH || image.getHeight() < MIN_HEIGHT) {
                throw new ImageProcessingException(
                    String.format("图片尺寸太小，最小要求%dx%d像素", MIN_WIDTH, MIN_HEIGHT)
                );
            }
        } catch (IOException e) {
            throw new ImageProcessingException("图片验证失败: " + e.getMessage());
        }
    }
} 