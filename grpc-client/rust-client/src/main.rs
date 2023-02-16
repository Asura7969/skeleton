use std::collections::HashMap;
use proto::{
    hello_service_client::{HelloServiceClient},
    HelloReply, HelloRequest,
};
use tonic::{Request, Response as TonicResponse, Status};
use tonic::transport::Endpoint;


mod proto {
    // 需要与proto文件中 'package com.asura.grpc;' 一致
    tonic::include_proto!("com.asura.grpc");
}

#[tokio::main]
async fn main() -> Result<(), Box<dyn std::error::Error>> {
    let addr = Endpoint::from_static("http://127.0.0.1:9091");
    let mut hello_cli = HelloServiceClient::connect(addr.clone()).await?;
    let request = Request::new(HelloRequest {
        name: "tonic".to_string(),
        tags: HashMap::new()
    });
    let response = hello_cli.say_hello(request).await?;
    println!("hello response: {:?}", response.into_inner());

    let mut goodbye_cli = HelloServiceClient::connect(addr).await?;
    let request = Request::new(HelloRequest {
        name: "tonic".to_string(),
        tags: HashMap::new()
    });
    let response = goodbye_cli.say_hello_out_stream(request).await?;
    println!("goodbye response: {:?}", response.into_inner());

    Ok(())
}
