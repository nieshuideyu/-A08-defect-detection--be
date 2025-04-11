package com.example.imagedetection.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId;
    
    @Column(name = "image_path", length = 255, nullable = false)
    private String imagePath;            // 原始图片路径
    
    @Column(name = "result_path", length = 255)
    private String resultPath;           // 结果图路径
    
    @Column(name = "heatmap_path", length = 255)
    private String heatmapPath;          // 热力图路径
    
    @Column(name = "detection_text", length = 1000)
    private String detectionText;        // 检测文本（JSON格式，包含置信度和标签）

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;
} 