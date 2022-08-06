package com.asura.akka.distributeddata;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.cluster.ddata.Key;
import akka.cluster.ddata.LWWMap;
import akka.cluster.ddata.LWWMapKey;
import akka.cluster.ddata.SelfUniqueAddress;
import akka.cluster.ddata.typed.javadsl.DistributedData;
import akka.cluster.ddata.typed.javadsl.Replicator;
import akka.cluster.ddata.typed.javadsl.ReplicatorMessageAdapter;
import lombok.extern.slf4j.Slf4j;
import scala.Option;

import java.util.Optional;

import static akka.cluster.ddata.typed.javadsl.Replicator.readLocal;
import static akka.cluster.ddata.typed.javadsl.Replicator.writeLocal;

/**
 * @author asura7969
 * @create 2022-07-24-22:40
 */
@Slf4j
public class ReplicatedCache {

    private final ReplicatorMessageAdapter<Command, LWWMap<String, String>> replicator;
    private final SelfUniqueAddress node;

    public ReplicatedCache(
            ActorContext<Command> context,
            ReplicatorMessageAdapter<Command, LWWMap<String, String>> replicator
    ) {
        this.replicator = replicator;
        node = DistributedData.get(context.getSystem()).selfUniqueAddress();
    }

    public static Behavior<Command> create() {
        Behavior<Command> setup = Behaviors.setup(context ->
                DistributedData.withReplicatorMessageAdapter(
                        (ReplicatorMessageAdapter<Command, LWWMap<String, String>> replicator) ->
                                new ReplicatedCache(context, replicator).createBehavior()));
        log.info("Init ReplicatedCache successfully!");
        return setup;
    }


    public Behavior<Command> createBehavior() {
        return Behaviors
                .receive(Command.class)
                .onMessage(PutInCache.class, cmd -> receivePutInCache(cmd.key, cmd.value))
                .onMessage(Evict.class, cmd -> receiveEvict(cmd.key))
                .onMessage(GetFromCache.class, cmd -> receiveGetFromCache(cmd.key, cmd.replyTo, cmd.uuid))
                .onMessage(InternalGetResponse.class, this::onInternalGetResponse)
                .onMessage(InternalUpdateResponse.class, notUsed -> Behaviors.same())
                .build();
    }

    private Behavior<Command> receivePutInCache(String key, String value) {
        replicator.askUpdate(
                askReplyTo ->
                        new Replicator.Update<>(
                                dataKey(key),
                                LWWMap.empty(),
                                writeLocal(),
                                askReplyTo,
                                curr -> curr.put(node, key, value)
                        ),
                InternalUpdateResponse::new);

        return Behaviors.same();
    }

    private Behavior<Command> receiveEvict(String key) {
        replicator.askUpdate(
                askReplyTo ->
                        new Replicator.Update<>(
                                dataKey(key),
                                LWWMap.empty(),
                                writeLocal(),
                                askReplyTo,
                                curr -> curr.remove(node, key)
                        ),
                InternalUpdateResponse::new);

        return Behaviors.same();
    }

    private Behavior<Command> receiveGetFromCache(String key, ActorRef<Cached> replyTo, String uuid) {
        replicator.askGet(
                askReplyTo -> new Replicator.Get<>(dataKey(key), readLocal(), askReplyTo),
                rsp -> new InternalGetResponse(key, replyTo, rsp, uuid));

        return Behaviors.same();
    }

    private Behavior<Command> onInternalGetResponse(InternalGetResponse msg) {
        if (msg.rsp instanceof Replicator.GetSuccess) {
            Option<String> valueOption = ((Replicator.GetSuccess<LWWMap<String, String>>) msg.rsp).get(dataKey(msg.key)).get(msg.key);
            Optional<String> valueOptional = Optional.ofNullable(valueOption.isDefined() ? valueOption.get() : null);
            log.info("回复信息 k:{}", msg.key);
            msg.replyTo.tell(new Cached(msg.key, valueOptional, msg.uuid));
        } else if (msg.rsp instanceof Replicator.NotFound) {
            log.info("NotFound 回复信息 k:{}", msg.key);
            msg.replyTo.tell(new Cached(msg.key, Optional.empty(), msg.uuid));
        }
        return Behaviors.same();
    }

    private Key<LWWMap<String, String>> dataKey(String entryKey) {
        return LWWMapKey.create("cache-" + Math.abs(entryKey.hashCode() % 100));
    }
}
