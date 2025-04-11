package com.example.imagedetection.service;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

public interface ImageService {
    Map<String, Object> uploadImage(MultipartFile file);
    List<Map<String, Object>> detectImage(Long id);
    byte[] getImageData(Long id);
    List<Map<String, Object>> getHistory();
    void deleteImage(Long id);
}