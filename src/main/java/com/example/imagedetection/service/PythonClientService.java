package com.example.imagedetection.service;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

public interface PythonClientService {
    List<Map<String, Object>> detect(MultipartFile file);
    byte[] getResultImage(MultipartFile file);
    byte[] getHeatmap(MultipartFile file);
}