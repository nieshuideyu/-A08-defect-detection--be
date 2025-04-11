package com.example.imagedetection.service.impl;

import com.example.imagedetection.exception.ImageProcessingException;
import com.example.imagedetection.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
public class LocalStorageServiceImpl implements StorageService {

    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public String store(MultipartFile file, String filename, String subDir) {
        try {
            // 生成唯一文件名
            String uniqueFilename = UUID.randomUUID().toString() + "_" + filename;
            
            // 创建目录
            Path uploadDir = Paths.get(uploadPath, subDir);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            
            // 保存文件
            Path filePath = uploadDir.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), filePath);
            
            // 返回相对路径
            return subDir + "/" + uniqueFilename;
        } catch (IOException e) {
            log.error("文件存储失败: {}", e.getMessage());
            throw new ImageProcessingException("文件存储失败: " + e.getMessage());
        }
    }

    @Override
    public void delete(String filePath) {
        try {
            if (filePath != null && !filePath.isEmpty()) {
                Path path = Paths.get(uploadPath, filePath);
                Files.deleteIfExists(path);
            }
        } catch (IOException e) {
            log.error("文件删除失败: {}", e.getMessage());
            throw new ImageProcessingException("文件删除失败: " + e.getMessage());
        }
    }
} 