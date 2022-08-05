package com.asura.akka.distributeddata;

import akka.actor.typed.ActorRef;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

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
     * 同步获取值
     * @param key key
     * @param futureProcess 处理value的函数
     * @param timeout 超时时间
     * @throws InterruptedException
     */
    public void get(String key,
                    Consumer<Optional<String>> futureProcess,
                    Duration timeout) throws InterruptedException {
        String uuid = UUID.randomUUID().toString();
        ForwardActor.SyncTask syncTask = new ForwardActor.SyncTask(futureProcess);
        ForwardActor.addTask(uuid, syncTask);
        replicatedCache.tell(new GetFromCache(key, forwardRef, uuid));
        syncTask.latch.await(timeout.toMillis(), TimeUnit.MILLISECONDS);
        // NOTE: 执行到这有两种情况
        // （1）ForwardActor 中已经接收到对应的数据，已经remove过一次task,此处再次 removeTask, 不影响task
        // （2）获取value超时，需要 removeTask
        ForwardActor.removeTask(uuid);
    }

    /**
     * 异步获取值
     * @param key key
     * @param futureProcess 处理value的函数
     * @param timeout 超时时间
     */
    public void asyncGet(String key,
                    Consumer<Optional<String>> futureProcess,
                    Duration timeout) {
        String uuid = UUID.randomUUID().toString();
        ForwardActor.ASyncTask asyncTask = new ForwardActor.ASyncTask(futureProcess);
        ForwardActor.addTask(uuid, asyncTask);
        replicatedCache.tell(new GetFromCache(key, forwardRef, uuid));

        cachedThreadPool.execute(() -> {
            try {
                asyncTask.latch.await(timeout.toMillis(), TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                log.error("", e);
            }
            ForwardActor.removeTask(uuid);
        });
    }
}
