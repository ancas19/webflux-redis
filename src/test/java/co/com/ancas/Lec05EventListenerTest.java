package co.com.ancas;

import co.com.ancas.config.BaseTest;
import org.junit.jupiter.api.Test;
import org.redisson.api.DeletedObjectListener;
import org.redisson.api.ExpiredObjectListener;
import org.redisson.api.RBucketReactive;
import org.redisson.client.codec.StringCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.concurrent.TimeUnit;

public class Lec05EventListenerTest extends BaseTest {

    @Test
    void expiredEventTest(){
        RBucketReactive<String> bucket=this.redissonReactiveClient.getBucket("user:1:name", StringCodec.INSTANCE);
        Mono<Void> set=bucket.set("Andres",10, TimeUnit.SECONDS);
        Mono<Void> get=bucket.get()
                .doOnNext(System.out::println)
                .then();

        Mono<Void> event=bucket.addListener(new ExpiredObjectListener() {
            @Override
            public void onExpired(String s) {
                System.out.println("Experied key: "+s);
            }
        }).then();

        StepVerifier.create(set.concatWith(get).concatWith(event))
                .verifyComplete();

        sleep(11000);
    }

    @Test
    void deleteEventsTest(){
        RBucketReactive<String> bucket=this.redissonReactiveClient.getBucket("user:1:name", StringCodec.INSTANCE);
        Mono<Void> set=bucket.set("Andres",10, TimeUnit.SECONDS);

        Mono<Void> get=bucket.get()
                .doOnNext(System.out::println)
                .then();

        Mono<Void> event=bucket.addListener(new DeletedObjectListener() {
            @Override
            public void onDeleted(String s) {
                System.out.println("Deleted: "+s);
            }
        }).then();

        StepVerifier.create(set.concatWith(get).concatWith(event))
                .verifyComplete();

        sleep(11000);
    }
}
