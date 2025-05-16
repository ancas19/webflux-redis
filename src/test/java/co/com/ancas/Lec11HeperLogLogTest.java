package co.com.ancas;

import co.com.ancas.config.BaseTest;
import org.junit.jupiter.api.Test;
import org.redisson.api.RHyperLogLogReactive;
import org.redisson.client.codec.LongCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;


public class Lec11HeperLogLogTest extends BaseTest {

    @Test
    void count() {
        RHyperLogLogReactive<Long> count = this.redissonReactiveClient.getHyperLogLog("user:visits", LongCodec.INSTANCE);

        List<Long> longList1=LongStream.rangeClosed(1,25000)
                .boxed()
                .collect(Collectors.toList());

        List<Long> longList2=LongStream.rangeClosed(25001,75000)
                .boxed()
                .collect(Collectors.toList());

        List<Long> longList3=LongStream.rangeClosed(1,75000)
                .boxed()
                .collect(Collectors.toList());


        List<Long> longList4=LongStream.rangeClosed(50000,100_000)
                .boxed()
                .collect(Collectors.toList());

        Mono<Void> mono=Flux.just(longList1,
                longList2,
                longList3,
                longList4)
                .flatMap(count::addAll)
                .then();

        StepVerifier.create(mono)
                .verifyComplete();

        count.count()
                .doOnNext(System.out::println)
                .then()
                .subscribe();
    }
}
