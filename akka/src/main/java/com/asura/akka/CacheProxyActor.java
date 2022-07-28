package com.asura.akka;

import akka.actor.UntypedAbstractActor;
import com.asura.akka.distributeddata.Cached;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author asura7969
 * @create 2022-07-28-20:31
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CacheProxyActor extends UntypedAbstractActor {

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof Cached) {
            Cached c = (Cached)message;
            System.out.println(c.key + " : " + c.value.get());
        } else {
            unhandled(message);
        }
    }
}
