package com.example.imagedetection.controller;

import com.example.imagedetection.exception.ImageProcessingException;
import com.example.imagedetection.service.ImageService;
import com.example.imagedetection.util.ImageValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "服务正常运行");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadImage(@RequestParam("file") MultipartFile file) {
        log.info("收到图片上传请求: {}", file.getOriginalFilename());
        try {
            // 验证文件
            ImageValidator.validateImage(file);

            // 处理图片
            Map<String, Object> uploadResult = imageService.uploadImage(file);

            // 构建响应
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "文件上传成功");
            response.put("data", uploadResult);

            return ResponseEntity.ok(response);
        } catch (ImageProcessingException e) {
            log.warn("图片处理异常: {}", e.getMessage());
            return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("图片上传失败", e);
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "上传失败: " + e.getMessage());
        }
    }


    @GetMapping("/detect/{id}")
    public ResponseEntity<Map<String, Object>> detectImage(@PathVariable Long id) {
        try {
            log.info("收到图片检测请求，ID: {}", id);
            List<Map<String, Object>> results = imageService.detectImage(id);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            Map<String, Object> data = new HashMap<>();
            data.put("results", results);
            response.put("data", data);
            log.info("图片检测完成，结果数量: {}", results.size());
            return ResponseEntity.ok(response);
        } catch (ImageProcessingException e) {
            log.warn("图片检测异常: {}", e.getMessage());
            return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            log.error("检测失败", e);
            return buildErrorResponse(HttpStatus.SERVICE_UNAVAILABLE, "检测失败: " + e.getMessage());
        }
    }

    @GetMapping(value = "/get_initial_image/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getInitialImage(@PathVariable Long id) {
        try {
            log.info("收到获取原始图片请求，ID: {}", id);
            byte[] imageData = imageService.getImageData(id);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setContentLength(imageData.length);
            log.debug("返回图片数据，大小: {} bytes", imageData.length);
            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
        } catch (ImageProcessingException e) {
            log.error("获取原始图片失败: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/history")
    public ResponseEntity<Map<String, Object>> getHistory() {
        try {
            log.info("收到获取历史记录请求");
            List<Map<String, Object>> history = imageService.getHistory();
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("total", history.size());
            response.put("data", history);
            log.info("成功返回 {} 条历史记录", history.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("获取历史记录失败", e);
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "获取历史记录失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/images/{id}")
    public ResponseEntity<Map<String, Object>> deleteImage(@PathVariable Long id) {
        try {
            log.info("收到删除图片请求，ID: {}", id);
            imageService.deleteImage(id);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "删除成功");
            log.info("图片删除成功，ID: {}", id);
            return ResponseEntity.ok(response);
        } catch (ImageProcessingException e) {
            log.error("删除图片失败: {}", e.getMessage());
            return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("删除图片失败", e);
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "删除失败: " + e.getMessage());
        }
    }
    
    // 构建统一的错误响应
    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String message) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("code", status.value());
        errorResponse.put("message", message);
        return ResponseEntity.status(status).body(errorResponse);
    }
}
