package com.example.imagedetection.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String store(MultipartFile file, String filename, String subDir);
    void delete(String filePath);
} 