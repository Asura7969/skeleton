package com.asura.grpcclient.controller;

import com.asura.grpc.HelloReply;
import com.asura.grpc.HelloRequest;
import com.asura.grpc.HelloServiceGrpc;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HelloController {

    @Resource
    HelloServiceGrpc.HelloServiceBlockingStub helloServiceBlockingStub;

    /**
     * http://localhost:8098/hello
     */
    @RequestMapping("/hello")
    public String hello() {
        Map<String, String> tags = new HashMap<>();
        tags.put("1", "1");
        HelloRequest request = HelloRequest.newBuilder()
                .setName("Tom哥")
                .putAllTags(tags)
                .build();
        HelloReply helloResponse = helloServiceBlockingStub.sayHello(request);
        String result = helloResponse.getMessage();
        System.out.println(result);
        return result;
    }

}
