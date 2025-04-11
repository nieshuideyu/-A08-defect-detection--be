package com.example.imagedetection.util;

import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public class MultipartInputStreamFileResource extends InputStreamResource {
    private final String filename;

    public MultipartInputStreamFileResource(MultipartFile file) throws IOException {
        super(file.getInputStream());
        this.filename = file.getOriginalFilename();
    }
    @Override
    public String getFilename() {
        return this.filename;
    }

    @Override
    public long contentLength() {
        return -1;
    }
}
