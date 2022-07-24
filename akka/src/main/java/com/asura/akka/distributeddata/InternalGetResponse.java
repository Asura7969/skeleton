package com.asura.akka.distributeddata;

import akka.actor.typed.ActorRef;
import akka.cluster.ddata.LWWMap;
import akka.cluster.ddata.typed.javadsl.Replicator.GetResponse;

/**
 * @author asura7969
 * @create 2022-07-24-22:39
 */
public class InternalGetResponse implements InternalCommand {
    public final String key;
    public final ActorRef<Cached> replyTo;
    public final GetResponse<LWWMap<String, String>> rsp;

    public InternalGetResponse(
            String key, ActorRef<Cached> replyTo, GetResponse<LWWMap<String, String>> rsp
    ) {
        this.key = key;
        this.replyTo = replyTo;
        this.rsp = rsp;
    }
}
