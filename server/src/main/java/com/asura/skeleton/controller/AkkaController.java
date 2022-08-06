package com.asura.skeleton.controller;

import com.asura.akka.distributeddata.*;
import com.asura.common.annotation.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;


/**
 * @author asura7969
 * @create 2022-07-28-20:16
 */
@ResponseResult
@RestController
@RequestMapping("/akka")
public class AkkaController {

    @Autowired
    private AkkaMapManager akkaMapManager;


    @GetMapping(value = "/put/{key}/{value}")
    public void put(@PathVariable("key") String key,
                    @PathVariable("value") String value) {
        akkaMapManager.put(new PutInCache(key, value));
    }

    @GetMapping(value = "/get/{key}")
    public void get(@PathVariable("key") String key) {
        long start = System.currentTimeMillis();
        CompletableFuture<Optional<String>> future = akkaMapManager.asyncGet(key, Duration.ofSeconds(10));
        future.whenComplete((s, throwable) -> {
            long took = System.currentTimeMillis() - start;
            System.out.println("get cache took: " + took + " ms");
            System.out.println(s.get());
        });
        future.join();
    }

}
