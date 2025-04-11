package com.example.imagedetection.dto;

import lombok.Data;
import java.util.Map;

@Data
public class StatisticsDTO {
    private Map<String, Double> defectTypeDistribution; // 缺陷类型分布
    private Double overallDefectRate;                   // 总体缺陷率
    private Map<String, Integer> defectCountByType;     // 各类型缺陷数量
    private Map<String, Double> modelConfidence;        // 模型可信度
} 