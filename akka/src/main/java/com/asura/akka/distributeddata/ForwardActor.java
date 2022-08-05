package com.asura.akka.distributeddata;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.japi.function.Function;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

@Slf4j
public class ForwardActor extends AbstractBehavior<Cached> {

//    private final LinkedBlockingDeque<Cached> queue = new LinkedBlockingDeque<>();

    private final static Map<String, Task> readyTask = new ConcurrentHashMap<>();

    private final static ExecutorService executorService = new ThreadPoolExecutor(
            5,
            10,
            10L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(),
            new ThreadFactory() {
                final AtomicInteger num = new AtomicInteger(0);
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "akka-forward-async-" + num.getAndIncrement());
                }
            }, new ThreadPoolExecutor.CallerRunsPolicy());

    public ForwardActor(ActorContext<Cached> context) {
        super(context);
    }


    public static Behavior<Cached> create() {
        return Behaviors.setup(ForwardActor::new);
    }

    public static ExecutorService getExecutorService() {
        return executorService;
    }

    @Override
    public Receive<Cached> createReceive() {
        return newReceiveBuilder()
                .onAnyMessage((Function<Cached, Behavior<Cached>>) msg ->
                        Behaviors.receive((context, message) -> {
                            Task t = readyTask.get(message.uuid);
                            if (null != t) {
                                t.execute(message);
                                readyTask.remove(message.uuid);
                            }
                            return Behaviors.same();
                        }))
                .build();
    }

    public static void addTask(String uuid, Task task) {
        readyTask.put(uuid, task);
    }

    public static void removeTask(String uuid) {
        readyTask.remove(uuid);
    }

//    public Object syncGetMsg() throws InterruptedException {
//        return queue.takeFirst();
//    }
//
//    public CompletableFuture<Object> asyncGetMsg(Duration timeout) {
//        return CompletableFuture.supplyAsync(() -> {
//            try {
//                return queue.pollFirst(timeout.toMillis(), TimeUnit.MILLISECONDS);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }, getContext().getExecutionContext());
//    }
//
//    public CompletableFuture<Void> asyncMsg(Consumer<Object> process, Duration timeout) {
//        return CompletableFuture.runAsync(() -> {
//            try {
//                Object o = queue.pollFirst(timeout.toMillis(), TimeUnit.MILLISECONDS);
//                process.accept(o);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }, getContext().getExecutionContext());
//    }


    public static abstract class Task {
        public CountDownLatch latch = new CountDownLatch(1);

        abstract void execute(Cached message);
    }

    public static class SyncTask extends Task {

        private final Consumer<Optional<String>> process;

        public SyncTask(Consumer<Optional<String>> process) {
            this.process = process;
        }

        @Override
        void execute(Cached message) {
            latch.countDown();
            executorService.submit(() -> process.accept(message.value));
        }
    }

    public static class ASyncTask extends Task {
        private final Consumer<Optional<String>> process;
        public ASyncTask(Consumer<Optional<String>> process) {
            this.process = process;
        }
        @Override
        void execute(Cached message) {
            latch.countDown();
            executorService.submit(() -> process.accept(message.value));
        }
    }


}
