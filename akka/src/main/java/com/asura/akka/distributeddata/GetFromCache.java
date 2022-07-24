package com.asura.akka.distributeddata;

import akka.actor.typed.ActorRef;
/**
 * @author asura7969
 * @create 2022-07-24-22:31
 */
public class GetFromCache implements Command{
    public final String key;
    public final ActorRef<Cached> replyTo;

    public GetFromCache(String key, ActorRef<Cached> replyTo) {
        this.key = key;
        this.replyTo = replyTo;
    }
}
