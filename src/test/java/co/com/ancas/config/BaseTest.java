package co.com.ancas.config;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.redisson.api.RedissonReactiveClient;

import java.util.Objects;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseTest {

    private RedissonConfig redissonConfig = new RedissonConfig();
    protected RedissonReactiveClient redissonReactiveClient;

    @BeforeAll
    public  void setUp() {
        redissonReactiveClient = redissonConfig.getRedissonReactiveClient();
    }

    @AfterAll
    public void tearDown() {
        if (Objects.nonNull(redissonReactiveClient)) {
            redissonReactiveClient.shutdown();
        }
    }

    protected  void sleep(long miillis)  {
        try {
            Thread.sleep(miillis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
