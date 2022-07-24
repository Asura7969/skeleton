package com.asura.akka.distributeddata;

/**
 * @author asura7969
 * @create 2022-07-24-22:31
 */
public class PutInCache implements Command{
    public final String key;
    public final String value;

    public PutInCache(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
