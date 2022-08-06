package com.asura.akka.distributeddata;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.japi.function.Function;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;

@Slf4j
public class ForwardActor extends AbstractBehavior<Cached> {

    private final static Map<String, BlockingDeque<Optional<String>>> inbox = new ConcurrentHashMap<>();

    public ForwardActor(ActorContext<Cached> context) {
        super(context);
    }


    public static Behavior<Cached> create() {
        return Behaviors.setup(ForwardActor::new);
    }

    @Override
    public Receive<Cached> createReceive() {
        return newReceiveBuilder()
                .onAnyMessage((Function<Cached, Behavior<Cached>>) msg ->
                        Behaviors.receive((context, message) -> {
                            try {
                                log.info("ForwardActor 接收到msg key: {}", message.key);
                                // todo: 不能插入null
                                inbox.get(message.uuid).offerLast(message.value);
                            } catch (NullPointerException npe) {
                                // ignore
                            }
                            return Behaviors.same();
                        }))
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
