package co.com.ancas;

import co.com.ancas.config.BaseTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucketReactive;
import org.redisson.api.RTransactionReactive;
import org.redisson.api.TransactionOptions;
import org.redisson.client.codec.LongCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec14TransactionTest extends BaseTest {

    private RBucketReactive<Long> user1Balance;
    private RBucketReactive<Long> user2Balance;

    @BeforeAll
    void accountSetUp() {
        user1Balance = this.redissonReactiveClient.getBucket("user:1:balance", LongCodec.INSTANCE);
        user2Balance = this.redissonReactiveClient.getBucket("user:2:balance", LongCodec.INSTANCE);
        Mono<Void> mono = user1Balance.set(100L)
                .then(user2Balance.set(0L))
                .then();

        StepVerifier.create(mono)
                .verifyComplete();
    }


    @AfterAll
    void accountTearDown() {
        Mono<Void> mono = Flux.zip(this.user1Balance.get(), this.user2Balance.get())
                .doOnNext(System.out::println)
                .then();

        StepVerifier.create(mono)
                .verifyComplete();
    }

    @Test
    void nonTransactionTest() {
        this.transfer(user1Balance, user2Balance, 50)
                .thenReturn(0)
                .map(i -> (5 / i))
                .doOnError(System.out::println)
                .subscribe();
        sleep(1000);

    }


    @Test
    void transactionTest() {
        RTransactionReactive transaction = this.redissonReactiveClient.createTransaction(TransactionOptions.defaults());
        RBucketReactive<Long> user1balence= transaction.getBucket("user:1:balance", LongCodec.INSTANCE);
        RBucketReactive<Long> user2balence= transaction.getBucket("user:2:balance", LongCodec.INSTANCE);
        this.transfer(user1balence, user2balence, 50)
                .thenReturn(0)
                ///.map(i -> (5 / i)) // some error
                .then(transaction.commit())
                .doOnError(System.out::println)
                .onErrorResume(ex -> transaction.rollback())
                .subscribe();
        sleep(1000);

    }


    private Mono<Void> transfer(RBucketReactive<Long> fromAccount, RBucketReactive<Long> toAccount, Integer amount) {
        return Flux.zip(fromAccount.get(), toAccount.get())
                .filter(t -> t.getT1() >= amount)
                .flatMap(t -> fromAccount.set(t.getT1() - amount).thenReturn(t))
                .flatMap(t -> toAccount.set(t.getT2() + amount))
                .then();
    }

}
