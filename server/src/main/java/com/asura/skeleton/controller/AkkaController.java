package com.asura.skeleton.controller;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.typed.ActorRef;
import akka.actor.typed.javadsl.Adapter;
import com.asura.akka.distributeddata.Cached;
import com.asura.akka.distributeddata.Command;
import com.asura.akka.distributeddata.GetFromCache;
import com.asura.akka.distributeddata.PutInCache;
import com.asura.common.annotation.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author asura7969
 * @create 2022-07-28-20:16
 */
@ResponseResult
@RestController
@RequestMapping("/akka")
public class AkkaController {

    @Autowired
    @Qualifier(value = "actorSystem")
    private ActorSystem system;
    @Autowired
    @Qualifier(value = "replicatedCache")
    private ActorRef<Command> replicatedCache;


    @PostMapping(value = "/put")
    public void put() {
        replicatedCache.tell(new PutInCache("key1", "A"));
    }

    @GetMapping(value = "/get")
    public void get() {
        final ActorRef<Cached> proxy = Adapter.spawnAnonymous(system, Cached.createBehavior());
        replicatedCache.tell(new GetFromCache("key1", proxy));
    }

}
