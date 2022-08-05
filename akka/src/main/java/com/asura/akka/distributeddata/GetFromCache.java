package com.asura.akka.distributeddata;

import akka.actor.typed.ActorRef;

/**
 * @author asura7969
 * @create 2022-07-24-22:31
 */
public class GetFromCache implements Command{
    public final String key;
    public final ActorRef<Cached> replyTo;
    public final String uuid;

    public GetFromCache(String key, ActorRef<Cached> replyTo, String uuid) {
        this.key = key;
        this.replyTo = replyTo;
        this.uuid = uuid;
    }
}
