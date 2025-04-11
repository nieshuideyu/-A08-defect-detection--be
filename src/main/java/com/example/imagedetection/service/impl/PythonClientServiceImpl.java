package com.example.imagedetection.service.impl;

import com.example.imagedetection.exception.ImageProcessingException;
import com.example.imagedetection.service.PythonClientService;
import com.example.imagedetection.util.MultipartInputStreamFileResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PythonClientServiceImpl implements PythonClientService {

    @Value("${python.server.url}")
    private String pythonServerUrl;
    
    @Value("${python.server.connection-timeout:5000}")
    private int connectionTimeout;
    
    @Value("${python.server.read-timeout:10000}")
    private int readTimeout;

    private final RestTemplate restTemplate;
    
    public PythonClientServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder
            .setConnectTimeout(Duration.ofMillis(connectionTimeout))
            .setReadTimeout(Duration.ofMillis(readTimeout))
            .build();
        log.info("初始化PythonClientService，服务地址: {}, 连接超时: {}ms, 读取超时: {}ms", 
                pythonServerUrl, connectionTimeout, readTimeout);
    }

    @Override
    public List<Map<String, Object>> detect(MultipartFile file) {
        String url = pythonServerUrl + "/api/python/detect";
        log.debug("发送图片检测请求到: {}", url);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        try {
            Resource resource = new MultipartInputStreamFileResource(file);
            body.add("image", resource);
        } catch (Exception e) {
            throw new ImageProcessingException("构造文件资源失败: " + e.getMessage());
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, requestEntity, Map.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                log.debug("Python检测接口返回成功，结果数量: {}", 
                        ((List<Map<String, Object>>) response.getBody().get("results")).size());
                return (List<Map<String, Object>>) response.getBody().get("results");
            } else {
                throw new ImageProcessingException("Python detect接口调用失败: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("调用Python detect接口异常: {}", e.getMessage());
            throw new ImageProcessingException("调用Python detect接口异常: " + e.getMessage());
        }
    }

    @Override
    public byte[] getResultImage(MultipartFile file) {
        String url = pythonServerUrl + "/api/python/result_image";
        return callPythonForImage(file, url);
    }

    @Override
    public byte[] getHeatmap(MultipartFile file) {
        String url = pythonServerUrl + "/api/python/heatmap";
        return callPythonForImage(file, url);
    }

    private byte[] callPythonForImage(MultipartFile file, String url) {
        log.debug("发送图片处理请求到: {}", url);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        try {
            Resource resource = new MultipartInputStreamFileResource(file);
            body.add("image", resource);
        } catch (Exception e) {
            throw new ImageProcessingException("构造文件资源失败: " + e.getMessage());
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        try {
            ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, byte[].class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                log.debug("Python接口返回成功，图片大小: {} bytes", response.getBody().length);
                return response.getBody();
            } else {
                throw new ImageProcessingException("Python接口调用失败: " + url + ", 状态码: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("调用Python接口失败: {}", e.getMessage());
            throw new ImageProcessingException("调用Python接口失败: " + e.getMessage());
        }
    }
}
