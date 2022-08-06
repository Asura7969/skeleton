package com.asura.akka.distributeddata;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;
import akka.japi.function.Function;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;

@Slf4j
public class ForwardActor {

    private final static Map<String, BlockingDeque<Optional<String>>> inbox = new ConcurrentHashMap<>();

    public ForwardActor() {
    }

    public static Behavior<Cached> create() {
        return Behaviors.setup(ctx -> new ForwardActor().createReceive());
    }

    public Behavior<Cached> createReceive() {
        return Behaviors
                .receive(Cached.class)
                .onAnyMessage((Function<Cached, Behavior<Cached>>) message -> {
                    try {
                        // note: 不能插入null
                        inbox.get(message.uuid).offerLast(message.value);
                    } catch (NullPointerException npe) {
                        // ignore
                    }
                    return Behaviors.same();
                })
                .build();
    }

    public static void addTask(String uuid) {
        inbox.put(uuid, new LinkedBlockingDeque<>(1));

    }

    public static void removeMsg(String uuid) {
        inbox.remove(uuid);
    }

    public static Optional<String> poll(String uuid, Duration timeout) throws InterruptedException {
        return inbox.get(uuid).poll(timeout.toNanos(), TimeUnit.NANOSECONDS);
    }


}
