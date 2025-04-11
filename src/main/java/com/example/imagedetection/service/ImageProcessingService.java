package com.example.imagedetection.service;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface ImageProcessingService {
    String extractText(MultipartFile file);  // OCR文字识别
    List<String> detectLabels(MultipartFile file);  // 图像标签识别
    String processImage(MultipartFile file);  // 图像处理（如压缩、裁剪等）
} 