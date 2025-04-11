DROP TABLE IF EXISTS images;

CREATE TABLE images (
    image_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    image_path VARCHAR(255) NOT NULL,
    result_path VARCHAR(255),
    heatmap_path VARCHAR(255),
    detection_text VARCHAR(1000)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4; 