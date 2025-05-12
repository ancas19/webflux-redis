package co.com.ancas;

import co.com.ancas.config.BaseTest;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucketReactive;
import org.redisson.client.codec.StringCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.concurrent.TimeUnit;

public class Lec01KeyValueTest extends BaseTest {

    @Test
    void keyValueAccessTest(){
        RBucketReactive<String> bucket=this.redissonReactiveClient.getBucket("user:1:name", StringCodec.INSTANCE);
        Mono<Void> set=bucket.set("Andres");
        Mono<Void> get=bucket.get()
                .doOnNext(System.out::println)
                .then();
        StepVerifier.create(set.concatWith(get))
                .verifyComplete();
    }

    @Test
    void keyValueExpiryTest(){
        RBucketReactive<String> bucket=this.redissonReactiveClient.getBucket("user:1:name", StringCodec.INSTANCE);
        Mono<Void> set=bucket.set("Andres",30, TimeUnit.SECONDS);
        Mono<Void> get=bucket.get()
                .doOnNext(System.out::println)
                .then();
        StepVerifier.create(set.concatWith(get))
                .verifyComplete();
    }

    @Test
    void keyValueExtendExpiryTest(){
        RBucketReactive<String> bucket=this.redissonReactiveClient.getBucket("user:1:name", StringCodec.INSTANCE);
        Mono<Void> set=bucket.set("Andres",30, TimeUnit.SECONDS);
        Mono<Void> get=bucket.get()
                .doOnNext(System.out::println)
                .then();
        StepVerifier.create(set.concatWith(get))
                .verifyComplete();

        //Extend
        sleep(5000);
        Mono<Boolean> mono=bucket.expire(60,TimeUnit.SECONDS);
        StepVerifier.create(mono)
                .expectNext(true)
                .verifyComplete();


        // Acces to expiration time
        Mono<Void> ttl=bucket.remainTimeToLive()
                .doOnNext(System.out::println)
                .then();

        StepVerifier.create(ttl)
                .verifyComplete();
    }
}
