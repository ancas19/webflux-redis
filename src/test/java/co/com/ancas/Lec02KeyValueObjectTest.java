package co.com.ancas;

import co.com.ancas.config.BaseTest;
import co.com.ancas.dto.Student;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucketReactive;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

public class Lec02KeyValueObjectTest extends BaseTest {

    @Test
    void keyValueObjectTest(){
        Student student= new Student("Andres", 30, "Medellin", List.of(12,45,67));
        RBucketReactive<Student> bucket=this.redissonReactiveClient.getBucket("student:1", new TypedJsonJacksonCodec(Student.class));
        Mono<Void> set=bucket.set(student);
        Mono<Void> get=bucket.get()
                .doOnNext(System.out::println)
                .then();

        StepVerifier.create(set.concatWith(get))
                .verifyComplete();
    }
}
