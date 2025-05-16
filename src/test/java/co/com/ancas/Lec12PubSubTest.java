package co.com.ancas;

import co.com.ancas.config.BaseTest;
import org.junit.jupiter.api.Test;
import org.redisson.api.RPatternTopicReactive;
import org.redisson.api.RTopicReactive;
import org.redisson.api.listener.PatternMessageListener;
import org.redisson.client.codec.StringCodec;

public class Lec12PubSubTest extends BaseTest {

    @Test
    void subscriber1() {
        RTopicReactive topic=this.redissonReactiveClient.getTopic("slack-room1", StringCodec.INSTANCE);
        topic.getMessages(String.class)
                .doOnError(System.out::println)
                .doOnNext(System.out::println)
                .subscribe();

        sleep(600_000);
    }

    @Test
    void subscriber2() {
        RPatternTopicReactive topic=this.redissonReactiveClient.getPatternTopic("slack-room*", StringCodec.INSTANCE);
        topic.addListener(String.class, new PatternMessageListener<String>() {
                    @Override
                    public void onMessage(CharSequence pattern, CharSequence channel, String msg) {
                        System.out.println(pattern+" "+channel+" "+msg);
                    }
                })
                .doOnError(System.out::println)
                .doOnNext(System.out::println)
                .subscribe();

        sleep(600_000);
    }




}
