package co.com.ancas;

import co.com.ancas.config.BaseTest;
import org.junit.jupiter.api.Test;
import org.redisson.client.codec.StringCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec04BucketAsMapTest extends BaseTest {

    @Test
    void bucketsAsMap() {
        //user:1:name
        //user:2:name
        //user:3:name

        Mono<Void> mono = this.redissonReactiveClient.getBuckets(StringCodec.INSTANCE)
                .get("user:1:name", "user:2:name", "user:3:name")
                .doOnNext(System.out::println)
                .then();

        StepVerifier.create(mono)
                .verifyComplete();
    }
}
