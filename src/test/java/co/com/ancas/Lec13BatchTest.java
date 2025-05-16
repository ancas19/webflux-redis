package co.com.ancas;

import co.com.ancas.config.BaseTest;
import org.junit.jupiter.api.Test;
import org.redisson.api.*;
import org.redisson.client.codec.LongCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec13BatchTest extends BaseTest {

    @Test
    void batchTest() {
        RBatchReactive batch = this.redissonReactiveClient.createBatch(BatchOptions.defaults());
        RListReactive<Long> list = batch.getList("numbers-list", LongCodec.INSTANCE);
        RSetReactive<Long> set = batch.getSet("numbers-set", LongCodec.INSTANCE);

        for (Long i = 0L; i < 500_000L; i++) {
            list.add(i);
            set.add(i);
        }

        StepVerifier.create(batch.execute().then())
                .verifyComplete();

    }

    @Test
    void regularTest() {
        RListReactive<Long> list = redissonReactiveClient.getList("numbers-list", LongCodec.INSTANCE);
        RSetReactive<Long> set = redissonReactiveClient.getSet("numbers-set", LongCodec.INSTANCE);
        Mono<Void> mono = Flux.range(1, 500_000)
                .map(Long::valueOf)
                .flatMap(i -> list.add(i).then(set.add(i)))
                .then();

        StepVerifier.create(mono)
                .verifyComplete();

    }
}
