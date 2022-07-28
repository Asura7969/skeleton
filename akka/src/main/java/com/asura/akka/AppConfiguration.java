package com.asura.akka;

import akka.actor.ActorSystem;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Props;
import akka.actor.typed.javadsl.Adapter;
import akka.cluster.typed.Cluster;
import com.asura.akka.distributeddata.Command;
import com.asura.akka.distributeddata.ReplicatedCache;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.asura.akka.SpringExtension.SPRING_EXTENSION_PROVIDER;

@Slf4j
@Configuration
public class AppConfiguration {

    private ApplicationContext applicationContext;

    private AkkaConfig akkaConfig;

    @Autowired
    public AppConfiguration(ApplicationContext applicationContext, AkkaConfig akkaConfig) {
        this.applicationContext = applicationContext;
        this.akkaConfig = akkaConfig;
    }

    private String configString() {
        StringBuilder seedNodes = new StringBuilder();
        for (int i = 0; i < akkaConfig.getSeed_nodes().size(); i++) {
            String n = akkaConfig.getSeed_nodes().get(i);
            String node = String.format("akka.cluster.seed-nodes.%s=\"akka://%s@%s\"\n",
                    i, akkaConfig.getName(), n);
            seedNodes.append(node);
        }
        return String.format(
                "akka.actor.provider=\"cluster\"\n" +
                "akka.remote.artery.canonical.hostname=\"127.0.0.1\"\n" +
                "akka.remote.artery.canonical.port=%s\n" +
                "%s" +
                "akka.cluster.downing-provider-class=\"akka.cluster.sbr.SplitBrainResolverProvider\"\n",
                akkaConfig.getRemote_port(),
                seedNodes);
    }

    @Bean("actorSystem")
    public ActorSystem actorSystem() {
        final String confStr = configString();
        log.info("akkConf: {}", confStr);
        Config myConfig = ConfigFactory.parseString(confStr);

        ActorSystem system = ActorSystem.create("akka-spring-skeleton", myConfig);
        SPRING_EXTENSION_PROVIDER.get(system).initialize(applicationContext);
        return system;
    }

    @Bean("cluster")
    public Cluster cluster(@Qualifier("actorSystem") ActorSystem system) {
        return Cluster.get(Adapter.toTyped(system));
    }

    @Bean("replicatedCache")
    public ActorRef<Command> replicatedCache(@Qualifier("actorSystem") ActorSystem system) {
        return Adapter.spawnAnonymous(system, ReplicatedCache.create(), Props.empty());
    }


}
