package com.asura.skeleton;

import com.asura.grpcserver.annotation.GrpcService;
import com.asura.grpcserver.runner.ServiceManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.util.Map;

/**
 * @author gongwenzhou
 */
@EnableFeignClients(basePackages = {"com.asura.open.api"})
@SpringBootApplication(scanBasePackages = "com.asura")
public class SkeletonApplication extends SpringBootServletInitializer {

    public static void main(String[] args) throws IOException, InterruptedException {
//        SpringApplication.run(SkeletonApplication.class, args);

        ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(SkeletonApplication.class, args);
        Map<String, Object> grpcServiceBeanMap = configurableApplicationContext.getBeansWithAnnotation(GrpcService.class);
        ServiceManager serviceManager = configurableApplicationContext.getBean(ServiceManager.class);
        serviceManager.loadService(grpcServiceBeanMap);
    }



}
