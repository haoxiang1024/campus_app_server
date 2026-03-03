package com.school.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class SpringMvcSupport implements WebMvcConfigurer {
    @Autowired
    private UserStatusInterceptor userStatusInterceptor;
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
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userStatusInterceptor)
                .addPathPatterns("/**")          // 所有接口
                .excludePathPatterns("/login");  // 排除登录接口
    }
}