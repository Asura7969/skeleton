package com.asura.akka;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import akka.actor.typed.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.RecipientRef;
import akka.actor.typed.javadsl.Adapter;
import akka.actor.typed.javadsl.AskPattern;
import akka.japi.function.Function;
import akka.util.Timeout;
import com.asura.akka.distributeddata.Cached;
import com.asura.akka.distributeddata.Command;
import com.asura.akka.distributeddata.GetFromCache;
import com.asura.akka.distributeddata.PutInCache;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import scala.concurrent.Await;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import scala.concurrent.duration.FiniteDuration;
import scala.util.Try;

import static akka.pattern.Patterns.ask;

import static com.asura.akka.SpringExtension.SPRING_EXTENSION_PROVIDER;

@ContextConfiguration(classes = AppConfiguration.class)
public class SpringAkkaIntegrationTest extends AbstractJUnit4SpringContextTests {

    @Autowired
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
        final akka.actor.typed.ActorSystem<Void> voidActorSystem = Adapter.toTyped(system);
        Duration timeout = Duration.ofSeconds(3);
        final Function<ActorRef<Command>, Command> messageFactory = ref -> {
            ref.tell(new PutInCache("key1", "A"));
            return new PutInCache("key1", "A");
        };
        AskPattern.ask(replicatedCache, messageFactory, timeout, voidActorSystem.scheduler());
        TestProbe<Cached> probe = TestProbe.create(voidActorSystem);
        replicatedCache.tell(new GetFromCache("key", probe.ref()));
        probe.expectMessage(new Cached("key1", Optional.of("A")));
    }

    @After
    public void tearDown() {

//        system.terminate().onComplete();
//        system.shutdown();
//        system.awaitTermination();
    }
}
