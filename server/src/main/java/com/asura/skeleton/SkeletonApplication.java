package com.asura.skeleton;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author gongwenzhou
 */
@EnableFeignClients(basePackages = {"com.asura.open.api"})
@SpringBootApplication(scanBasePackages = "com.asura")
public class SkeletonApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SkeletonApplication.class, args);
    }

}
