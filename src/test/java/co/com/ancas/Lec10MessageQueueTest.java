package co.com.ancas;

import co.com.ancas.config.BaseTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBlockingDequeReactive;
import org.redisson.client.codec.LongCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

public class Lec10MessageQueueTest extends BaseTest {

    private RBlockingDequeReactive<Long> msgQueue;

    @BeforeAll
    void setUpQueu() {
        this.msgQueue = this.redissonReactiveClient.getBlockingDeque("message-queue", LongCodec.INSTANCE);
    }


    @Test
    void consumer1(){
        this.msgQueue.takeElements()
                .doOnNext(i->System.out.println("Consumer 1: "+i))
                .doOnError(System.out::println)
                .subscribe();


        sleep(600_000);
    }

    @Test
    void consumer2(){
        this.msgQueue.takeElements()
                .doOnNext(i->System.out.println("Consumer 2: "+i))
                .doOnError(System.out::println)
                .subscribe();


        sleep(600_000);
    }


    @Test
    void producer(){
        Mono<Void> mono=Flux.range(1,100)
                .delayElements(Duration.ofMillis(500))
                .doOnNext(i->System.out.println("Going to add: "+i))
                .map(Long::valueOf)
                .flatMap(i->this.msgQueue.addFirst(i))
                .then();
        StepVerifier.create(mono)
                        .verifyComplete();

        sleep(600_000);
    }
}
