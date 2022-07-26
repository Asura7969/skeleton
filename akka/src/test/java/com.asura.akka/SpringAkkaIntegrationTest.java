package com.asura.akka;

import java.time.Duration;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import akka.actor.typed.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.javadsl.Adapter;
import akka.actor.typed.javadsl.AskPattern;
import akka.cluster.typed.Cluster;
import akka.japi.function.Function;
import akka.remote.testconductor.RoleName;
import akka.remote.testkit.MultiNodeConfig;
import akka.remote.testkit.MultiNodeSpec;
import akka.util.Collections;
import akka.util.Timeout;
import com.asura.akka.distributeddata.Cached;
import com.asura.akka.distributeddata.Command;
import com.asura.akka.distributeddata.GetFromCache;
import com.asura.akka.distributeddata.PutInCache;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import scala.Function0;
import scala.collection.JavaConverters;
import scala.collection.immutable.Seq;
import scala.concurrent.Await;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import scala.concurrent.duration.FiniteDuration;
import scala.runtime.BoxedUnit;
import scala.util.Try;

import static akka.pattern.Patterns.ask;

import static com.asura.akka.SpringExtension.SPRING_EXTENSION_PROVIDER;

@ContextConfiguration(classes = AppConfiguration.class)
public class SpringAkkaIntegrationTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    @Qualifier(value = "actorSystem")
    private ActorSystem system;

    @Autowired
    @Qualifier(value = "replicatedCache")
    private ActorRef<Command> replicatedCache;

    @Test
    public void whenCallingGreetingActor_thenActorGreetsTheCaller() throws Exception {
        akka.actor.ActorRef greeter = system.actorOf(SPRING_EXTENSION_PROVIDER.get(system).props("greetingActor"), "greeter");

        FiniteDuration duration = FiniteDuration.create(5, TimeUnit.SECONDS);
        Timeout timeout = Timeout.durationToTimeout(duration);

        Future<Object> result = ask(greeter, new GreetingActor.Greet("John"), timeout);

        Assert.assertEquals("Hello, John", Await.result(result, duration));
    }

    @Test
    public void cache_test() {
        final akka.actor.typed.ActorSystem<Void> typedSystem = Adapter.toTyped(system);
        final BaseNodeSpec spec = new BaseNodeSpec(new ReplicatedCacheSpec());
        spec.put();
//        final Cluster cluster = Cluster.createExtension(typedSystem);
//        Duration timeout = Duration.ofSeconds(3);
//        final Function<ActorRef<Command>, Command> messageFactory = ref -> {
//            ref.tell(new PutInCache("key1", "A"));
//            return new PutInCache("key1", "A");
//        };
//        AskPattern.ask(replicatedCache, messageFactory, timeout, typedSystem.scheduler());
        TestProbe<Cached> probe = TestProbe.create(typedSystem);
        replicatedCache.tell(new GetFromCache("key1", probe.ref()));
        probe.expectMessage(Duration.ofSeconds(10), new Cached("key1", Optional.of("A")));
    }

    @After
    public void tearDown() {

//        system.terminate().onComplete();
//        system.shutdown();
//        system.awaitTermination();
    }

    private static class ReplicatedCacheSpec extends MultiNodeConfig {
        public RoleName node1 = role("node-1");

        @Override
        public void commonConfig(Config config) {
            final Config c = ConfigFactory.parseString(
                    "akka.loglevel = INFO\n" +
                    "akka.actor.provider = \"cluster\"\n" +
                    "akka.log-dead-letters-during-shutdown = off\n");
            super.commonConfig(c);
        }
    }

    private class BaseNodeSpec extends MultiNodeSpec {

        private ReplicatedCacheSpec config;
        public BaseNodeSpec(ReplicatedCacheSpec config) {
            super(config);
            this.config = config;
        }

        @Override
        public int initialParticipants() {
            return roles().size();
        }

        public void put() {
            final Set<RoleName> roles = java.util.Collections.singleton(config.node1);
            final Seq<RoleName> roleList = JavaConverters.asScalaIteratorConverter(roles.iterator()).asScala().toSeq();
//            final akka.actor.typed.ActorSystem<Void> typedSystem = Adapter.toTyped(system);
            runOn(roleList, () -> {
//                Duration timeout = Duration.ofSeconds(3);
//                final Function<ActorRef<Command>, Command> messageFactory = ref -> {
//                    ref.tell(new PutInCache("key1", "A"));
//                    System.out.println("-----------------");
//                    return new PutInCache("key1", "A");
//                };
//                System.out.println("-----------------");
//                replicatedCache.narrow().tell(new PutInCache("key1", "A"));
                replicatedCache.tell(new PutInCache("key1", "A"));
//                AskPattern.ask(replicatedCache, messageFactory, timeout, typedSystem.scheduler());
                return BoxedUnit.UNIT;
            });
        }
    }
}
