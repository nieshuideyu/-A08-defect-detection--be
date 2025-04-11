package com.example.imagedetection.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class ImageResponseDTO {
    private Long id;                     // 图片ID
    private String text;                 // 检测结果文本
    private List<String> labels;         // 标签列表
    private String imageUrl;             // 原始图片URL
    private String message;              // 处理消息
    private String annotatedImageUrl;    // 标注后的图片URL
    private String heatmapUrl;          // 热力图URL
    private Double defectRate;          // 缺陷率
    private String defectType;          // 缺陷类型
    private List<String> defectLocations; // 缺陷位置列表
    private LocalDateTime uploadTime;    // 上传时间
    private Map<String, Double> confidenceScores; // 各项指标的置信度
} 