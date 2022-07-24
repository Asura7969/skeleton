package com.asura.akka;

import akka.actor.ActorSystem;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Props;
import com.asura.akka.distributeddata.Command;
import com.asura.akka.distributeddata.ReplicatedCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import akka.actor.typed.scaladsl.adapter.package$;


import static com.asura.akka.SpringExtension.SPRING_EXTENSION_PROVIDER;

@Configuration
@ComponentScan
public class AppConfiguration {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean("actorSystem")
    public ActorSystem actorSystem() {
        ActorSystem system = ActorSystem.create("akka-spring-skeleton");
        SPRING_EXTENSION_PROVIDER.get(system).initialize(applicationContext);
        return system;
    }

    @Bean("replicatedCache")
    public ActorRef<Command> replicatedCache(@Qualifier("actorSystem") ActorSystem system) {
        return package$.MODULE$.ClassicActorSystemOps(system).spawnAnonymous(ReplicatedCache.create(), Props.empty());
    }
}
