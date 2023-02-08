package com.asura.grpcclient.annotation;

import com.asura.grpc.HelloServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcServiceConfig {

    @Bean
    public ManagedChannel getChannel() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9091)
                .usePlaintext()
                .build();
        return channel;
}

    @Bean
    public HelloServiceGrpc.HelloServiceBlockingStub getStub1(ManagedChannel channel) {
        return HelloServiceGrpc.newBlockingStub(channel);
    }

}
