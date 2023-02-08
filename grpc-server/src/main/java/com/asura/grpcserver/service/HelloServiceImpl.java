package com.asura.grpcserver.service;


import com.asura.grpc.HelloReply;
import com.asura.grpc.HelloReplyList;
import com.asura.grpc.HelloRequest;
import com.asura.grpc.HelloServiceGrpc;
import com.asura.grpcserver.annotation.GrpcService;
import io.grpc.stub.StreamObserver;

import java.util.UUID;

@GrpcService
public class HelloServiceImpl extends HelloServiceGrpc.HelloServiceImplBase {

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        System.out.println("接收到客户端信息： " + request.getName());
        responseObserver.onNext(HelloReply.newBuilder().setMessage("张三").build());
        responseObserver.onCompleted();
    }

    @Override
    public void sayHelloOutStream(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        System.out.println("接收到客户端信息： " + request.getName());
        responseObserver.onNext(HelloReply.newBuilder().setMessage("张三").build());
        responseObserver.onNext(HelloReply.newBuilder().setMessage("李四").build());
        responseObserver.onNext(HelloReply.newBuilder().setMessage("王五").build());
        responseObserver.onNext(HelloReply.newBuilder().setMessage("赵六").build());

        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<HelloRequest> sayHelloInStream(StreamObserver<HelloReplyList> responseObserver) {
        return new StreamObserver<HelloRequest>() {

            @Override
            public void onNext(HelloRequest value) {
                System.out.println("onNext: " + value.getName());
            }

            @Override
            public void onError(Throwable t) {
                System.out.println(t.getMessage());
            }

            @Override
            public void onCompleted() {
                HelloReply hello1 = HelloReply.newBuilder()
                        .setMessage("张三").build();
                HelloReply hello2 = HelloReply.newBuilder()
                        .setMessage("李四").build();

                HelloReplyList list = HelloReplyList.newBuilder()
                        .addReply(hello1)
                        .addReply(hello2)
                        .build();

                responseObserver.onNext(list);
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public StreamObserver<HelloRequest> sayHelloBothStream(StreamObserver<HelloReply> responseObserver) {
        return new StreamObserver<HelloRequest>() {
            @Override
            public void onNext(HelloRequest value) {
                System.out.println(value.getName());
                System.out.println(value.getTagsMap());

                responseObserver.onNext(HelloReply.newBuilder()
                        .setMessage(UUID.randomUUID().toString()).build());
            }

            @Override
            public void onError(Throwable t) {
                System.out.println(t.getMessage());
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }
}
