# 图像检测系统

## 项目介绍

本项目是一个基于Spring Boot和Python的图像检测系统，能够对上传的图像进行智能分析和检测，识别图像中的特定目标，并提供检测结果的可视化展示。系统采用前后端分离架构，后端使用Java Spring Boot提供RESTful API服务，前端可以通过这些API进行图像上传、检测和结果查询等操作。

## 功能特点

- **图像上传**：支持JPEG、PNG、GIF等多种格式的图像文件上传，并进行文件格式和大小验证
- **图像检测**：利用Python后端服务对图像进行智能分析和目标检测
- **结果可视化**：生成检测结果图和热力图，直观展示检测结果
- **历史记录**：保存检测历史，方便用户查看和管理历史检测结果
- **数据管理**：支持删除不需要的图像和检测结果

## 技术架构

### 后端技术栈

- **Spring Boot 2.7.13**：核心框架，提供RESTful API
- **Spring Data JPA**：数据持久层框架
- **MySQL**：关系型数据库，存储图像元数据和检测结果
- **Lombok**：简化Java代码
- **Tess4j 5.8.0**：OCR文字识别库
- **Python服务**：提供图像检测算法支持

### 系统架构

```
+----------------+      +-------------------+      +----------------+
|                |      |                   |      |                |
|  客户端/前端    +----->+  Spring Boot API  +----->+  Python 服务    |
|                |      |                   |      |                |
+----------------+      +-------------------+      +----------------+
                                  |
                                  v
                        +-------------------+
                        |                   |
                        |  MySQL 数据库     |
                        |                   |
                        +-------------------+
```

## 安装说明

### 环境要求

- JDK 17
- Maven 3.6+
- MySQL 8.0+
- Python 3.8+（用于检测服务）

### 安装步骤

1. **克隆项目**

```bash
git clone [https://github.com/yourusername/image-detection-service.git](https://github.com/nieshuideyu/-A08-defect-detection--be.git)
cd image-detection-service
```

2. **配置数据库**

创建MySQL数据库，并更新`application.properties`中的数据库配置：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/image_detection?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. **配置上传路径**

在`application.properties`中设置图片上传路径：

```properties
upload.path=your_upload_path
```

4. **配置Python服务**

在`application.properties`中设置Python服务URL：

```properties
python.server.url=http://localhost:5000
```

5. **构建并运行项目**

```bash
mvn clean package
java -jar target/image-detection-service-1.0-SNAPSHOT.jar
```

## 使用方法

### API接口说明

#### 1. 健康检查

```
GET /api/health
```

#### 2. 上传图像

```
POST /api/upload
Content-Type: multipart/form-data

form-data:
  file: (图像文件)
```

#### 3. 检测图像

```
GET /api/detect/{id}
```

#### 4. 获取原始图像

```
GET /api/get_initial_image/{id}
```

#### 5. 获取历史记录

```
GET /api/history
```

#### 6. 删除图像

```
DELETE /api/images/{id}
```

### 示例调用

**上传图像**：

```bash
curl -X POST -F "file=@/path/to/your/image.jpg" http://localhost:8080/api/upload
```

**检测图像**：

```bash
curl -X GET http://localhost:8080/api/detect/1
```

## 注意事项

- 支持的图像格式：JPEG、PNG、GIF
- 最大文件大小：10MB
- 最小图像尺寸：100x100像素

## 许可证

[MIT License](LICENSE)
