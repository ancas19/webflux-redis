package co.com.ancas;

import co.com.ancas.config.BaseTest;
import org.junit.jupiter.api.Test;
import org.redisson.api.RScoredSortedSetReactive;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.function.Function;

public class Lec15SortedSetTest extends BaseTest {

    @Test
    void sortedSet() {
        RScoredSortedSetReactive<String> sortedSet = this.redissonReactiveClient.getScoredSortedSet("student:score");

        Mono<Void> mono=sortedSet.addScore("Sam",12.25)
                .then(sortedSet.add(23.25, "mike"))
                .then(sortedSet.addScore("jake",7))
                .then();

        StepVerifier.create(mono)
                .verifyComplete();

        sortedSet.entryRange(0,1)
                .flatMapIterable(Function.identity())//flux
                .map(se->se.getScore() + " : "+ se.getValue())
                .doOnNext(System.out::println)
                .subscribe();

        sleep(1000);
    }
}
