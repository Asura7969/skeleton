package com.asura.akka.distributeddata;

import akka.actor.typed.ActorRef;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class AkkaMapManager {

    private final ExecutorService cachedThreadPool = Executors.newCachedThreadPool(new ThreadFactory() {
        final AtomicInteger num = new AtomicInteger(0);
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "AkkaMapManager-wait-" + num.getAndIncrement());
        }
    });

    @Autowired
    @Qualifier(value = "replicatedCache")
    private ActorRef<Command> replicatedCache;

    @Autowired
    @Qualifier(value = "forwardRef")
    private ActorRef<Cached> forwardRef;

    public void put(PutInCache p) {
        replicatedCache.tell(p);
    }

    public void del(String key) {
        replicatedCache.tell(new Evict(key));
    }

    /**
     * 异步获取值
     * @param key key
     * @param timeout 超时时间
     */
    public CompletableFuture<Optional<String>> asyncGet(String key, Duration timeout) {
        return CompletableFuture.supplyAsync(() -> {
            String uuid = UUID.randomUUID().toString();
            ForwardActor.addTask(uuid);
            replicatedCache.tell(new GetFromCache(key, forwardRef, uuid));
            try {
                Optional<String> m = ForwardActor.poll(uuid, timeout);
                ForwardActor.removeMsg(uuid);
                return m;
            } catch (InterruptedException e) {
                log.error("", e);
                return Optional.empty();
            }
        }, cachedThreadPool);

    }
}
