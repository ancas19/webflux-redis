package co.com.ancas;

import co.com.ancas.config.BaseTest;
import org.junit.jupiter.api.Test;
import org.redisson.api.RListReactive;
import org.redisson.api.RQueueReactive;
import org.redisson.client.codec.LongCodec;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Lec09ListQueueStackTest extends BaseTest {


    @Test
    void listTest(){
        // lrange number-input 0 -1
        RListReactive<Long> list=this.redissonReactiveClient.getList("number-input", LongCodec.INSTANCE);

        List<Long> longList=LongStream.rangeClosed(1,10)
                .boxed()
                .collect(Collectors.toList());


        StepVerifier.create(list.addAll(longList).then())
                .verifyComplete();

        StepVerifier.create(list.size())
                .expectNext(40)
                .verifyComplete();
    }

    @Test
    void  queueTest(){
        RQueueReactive<Long> queue=this.redissonReactiveClient.getQueue("number-input", LongCodec.INSTANCE);
        queue.poll()
    }
}
