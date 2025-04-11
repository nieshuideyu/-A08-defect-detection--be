package com.example.imagedetection.repository;

import com.example.imagedetection.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    // 移除 findByUploadTimeBetween 方法，因为我们已经不使用 uploadTime 字段了
} 