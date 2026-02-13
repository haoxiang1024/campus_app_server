package com.school;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SchoolApplication {

    public static void main(String[] args) {
//        SpringApplicationBuilder builder = new SpringApplicationBuilder(SchoolApplication.class);
//        // 强制关闭 headless 模式
//        builder.headless(false).run(args);
        SpringApplication.run(SchoolApplication.class, args);

    }



}
