package com.school.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class SpringMvcSupport implements WebMvcConfigurer {
    @Value("${file.upload.path}")
    private String uploadPath;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        String path = uploadDir.getAbsolutePath();
        if (!path.endsWith(File.separator)) {
            path += File.separator;
        }
        String locations = "file:" + path.replace("\\", "/");
        registry.addResourceHandler("/upload/**")
                .addResourceLocations(locations);

        System.out.println("文件上传映射路径: " + locations);
    }
}