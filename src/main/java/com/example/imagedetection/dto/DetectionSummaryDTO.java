package com.example.imagedetection.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class DetectionSummaryDTO {
    private String overallConclusion;    // 总体结论
    private List<String> defectDetails;  // 缺陷详情列表
    private Map<String, Double> metrics; // 各项指标
    private String recommendation;       // 处理建议
    private Map<String, String> charts;  // 相关图表URLs
} 