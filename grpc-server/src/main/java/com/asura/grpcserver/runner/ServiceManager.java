package com.asura.grpcserver.runner;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
public class ServiceManager {

    private Server server;

    public void loadService(Map<String, Object> grpcServiceBeanMap) throws IOException, InterruptedException {
        int grpcServerPort = 9091;
        ServerBuilder<?> serverBuilder = ServerBuilder.forPort(grpcServerPort);
        // 采用注解扫描方式，添加服务
        for (Object bean : grpcServiceBeanMap.values()) {
            serverBuilder.addService((BindableService) bean);
            System.out.println(bean.getClass().getSimpleName() + " is register in Spring Boot！");
            log.info("{} is register in Spring Boot！", bean.getClass().getSimpleName());
        }
        server = serverBuilder.build().start();

        log.info("grpc server is started at {}", grpcServerPort);

        // 增加一个钩子，当JVM进程退出时，Server 关闭
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.error("*** shutting down gRPC server since JVM is shutting down");
            if (server != null) {
                server.shutdown();
            }
            log.error("*** gRPC server shutdown！！！！");
        }));
        server.awaitTermination();
    }
}
