package com.asura.akka.distributeddata;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;

import java.util.Optional;
import java.util.UUID;

/**
 * @author asura7969
 * @create 2022-07-24-22:32
 */
public class Cached {
    public final String key;
    public final Optional<String> value;
    public final String uuid;

    public Cached(String key, Optional<String> value, String uuid) {
        this.key = key;
        this.value = value;
        this.uuid = uuid;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Cached other = (Cached) obj;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Cached [key=" + key + ", value=" + value + "]";
    }


    public static Behavior<Cached> createBehavior() {
        return Behaviors
                .receive(Cached.class)
                .onMessage(Cached.class, c -> {
                    System.out.println(c.key + " : " + c.value.get());
                    return Behaviors.same();
                })
                .build();
    }
}
