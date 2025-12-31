package com.school.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.concurrent.TimeUnit;

@Configuration
public class SpringMvcSupport extends WebMvcConfigurationSupport {

    // 配置upload路径：school根目录下的upload（与src同级）
    @Value("${file.upload.path:./upload/}")
    private String uploadPath;

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 1. 原有static目录映射（保留）
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");

        // 2. 核心修正：映射upload目录
        // URL前缀：/upload/**（不要加/school，上下文路径会自动拼接）
        registry.addResourceHandler("/upload/**")
                // 关键：file: + 项目根目录下的upload（与src同级）
                .addResourceLocations("file:" + uploadPath)
                // 禁用缓存
                .setCacheControl(CacheControl.maxAge(0, TimeUnit.SECONDS));

        super.addResourceHandlers(registry);
    }
}