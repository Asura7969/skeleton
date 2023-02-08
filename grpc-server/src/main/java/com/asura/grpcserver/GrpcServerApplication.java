//package com.asura.grpcserver;
//
//import com.asura.grpcserver.annotation.GrpcService;
//import com.asura.grpcserver.runner.ServiceManager;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.ConfigurableApplicationContext;
//
//import java.io.IOException;
//import java.util.Map;
//
//@SpringBootApplication
//public class GrpcServerApplication {
//
//    public static void main(String[] args) throws IOException, InterruptedException {
//        ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(GrpcServerApplication.class, args);
//        Map<String, Object> grpcServiceBeanMap = configurableApplicationContext.getBeansWithAnnotation(GrpcService.class);
//        ServiceManager serviceManager = configurableApplicationContext.getBean(ServiceManager.class);
//        serviceManager.loadService(grpcServiceBeanMap);
//    }
//
//}
