package com.school.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
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
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        ApplicationHome home = new ApplicationHome(getClass());
        File baseDir = home.getDir();
        if (baseDir.getAbsolutePath().endsWith("classes") || baseDir.getAbsolutePath().endsWith("target")) {
            while (baseDir.getName().equals("classes") || baseDir.getName().equals("target")) {
                baseDir = baseDir.getParentFile();
            }
        }
        File uploadDir = new File(baseDir, "upload");
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        String location = "file:" + uploadDir.getAbsolutePath() + File.separator;
        registry.addResourceHandler("/upload/**")
                .addResourceLocations(location)
                .setCacheControl(CacheControl.maxAge(0, TimeUnit.SECONDS));

        super.addResourceHandlers(registry);
    }
}