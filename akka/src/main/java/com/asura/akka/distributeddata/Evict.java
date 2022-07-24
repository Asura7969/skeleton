package com.asura.akka.distributeddata;

/**
 * @author asura7969
 * @create 2022-07-24-22:38
 */
public class Evict implements Command {
    public final String key;

    public Evict(String key) {
        this.key = key;
    }
}
