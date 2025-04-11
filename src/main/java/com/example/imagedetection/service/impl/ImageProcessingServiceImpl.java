/*package com.example.imagedetection.service.impl;

import com.example.imagedetection.service.ImageProcessingService;
import com.example.imagedetection.exception.ImageProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
public class ImageProcessingServiceImpl implements ImageProcessingService {

    // 定义缺陷类型常量
    public static final List<String> DEFECT_TYPES = Arrays.asList(
        "夹杂物", "补丁", "划痕", "其他缺陷"
    );

    @Override
    public String extractText(MultipartFile file) {
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            // 随机选择一个缺陷类型作为示例
            String defectType = DEFECT_TYPES.get(new Random().nextInt(DEFECT_TYPES.size()));
            return String.format("检测到缺陷：%s\n位置：左上角区域\n严重程度：轻微", defectType);
        } catch (IOException e) {
            log.error("OCR处理失败", e);
            throw new ImageProcessingException("OCR处理失败: " + e.getMessage());
        }
    }

    @Override
    public List<String> detectLabels(MultipartFile file) {
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            // 随机选择一个缺陷类型作为示例
            String defectType = DEFECT_TYPES.get(new Random().nextInt(DEFECT_TYPES.size()));
            return Arrays.asList(defectType, "轻微缺陷", "金属表面");
        } catch (IOException e) {
            log.error("标签检测失败", e);
            throw new ImageProcessingException("标签检测失败: " + e.getMessage());
        }
    }

    @Override
    public String processImage(MultipartFile file) {
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            // 这里应该是实际的图像处理逻辑
            return "processed_" + file.getOriginalFilename();
        } catch (IOException e) {
            log.error("图像处理失败", e);
            throw new ImageProcessingException("图像处理失败: " + e.getMessage());
        }
    }
} */
package com.example.imagedetection.service.impl;

import com.example.imagedetection.service.ImageProcessingService;
import com.example.imagedetection.exception.ImageProcessingException;
import com.example.imagedetection.service.PythonClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ImageProcessingServiceImpl implements ImageProcessingService {

    @Autowired
    private PythonClientService pythonClientService;

    @Override
    public String extractText(MultipartFile file) {
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            // 调用 Python 后端进行缺陷检测
            List<Map<String, Object>> detectionResults = pythonClientService.detect(file);
            // 假设我们只取第一个检测结果作为示例
            if (!detectionResults.isEmpty()) {
                Map<String, Object> firstResult = detectionResults.get(0);
                String defectType = (String) firstResult.get("label");
                return String.format("检测到缺陷：%s\n位置：左上角区域\n严重程度：轻微", defectType);
            } else {
                return "未检测到缺陷";
            }
        } catch (IOException e) {
            log.error("OCR处理失败", e);
            throw new ImageProcessingException("OCR处理失败: " + e.getMessage());
        }
    }

    @Override
    public List<String> detectLabels(MultipartFile file) {
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            // 调用 Python 后端进行缺陷检测
            List<Map<String, Object>> detectionResults = pythonClientService.detect(file);
            // 提取所有缺陷标签
            return detectionResults.stream()
                    .map(result -> (String) result.get("label"))
                    .toList();
        } catch (IOException e) {
            log.error("标签检测失败", e);
            throw new ImageProcessingException("标签检测失败: " + e.getMessage());
        }
    }

    @Override
    public String processImage(MultipartFile file) {
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            // 这里应该是实际的图像处理逻辑
            return "processed_" + file.getOriginalFilename();
        } catch (IOException e) {
            log.error("图像处理失败", e);
            throw new ImageProcessingException("图像处理失败: " + e.getMessage());
        }
    }
}