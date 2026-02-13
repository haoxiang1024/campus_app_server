package com.school.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import java.io.File;
import java.util.concurrent.TimeUnit;

@Configuration
public class SpringMvcSupport extends WebMvcConfigurationSupport {

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 1. 保留静态资源映射
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");

        // 2. 动态获取 upload 路径
        String userDir = System.getProperty("user.dir");
        File uploadDir = new File(userDir, "upload");

        // 调试打印：这一行非常关键！
        // 启动后看控制台，确认它打印的路径下是否真的有你的图片文件
        System.out.println(">>> [DEBUG] 当前程序寻找文件的物理路径是: " + uploadDir.getAbsolutePath());

        String location = "file:" + uploadDir.getAbsolutePath() + File.separator;

        registry.addResourceHandler("/upload/**")
                .addResourceLocations(location)
                .setCacheControl(CacheControl.maxAge(0, TimeUnit.SECONDS));

        super.addResourceHandlers(registry);
    }
}