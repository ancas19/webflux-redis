package co.com.ancas;

import co.com.ancas.config.BaseTest;
import co.com.ancas.dto.Student;
import org.junit.jupiter.api.Test;
import org.redisson.api.RMapReactive;
import org.redisson.client.codec.StringCodec;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Lec06MapTest extends BaseTest {

    @Test
    void mapTest() {
        RMapReactive<String, String> map = this.redissonReactiveClient.getMap("user:1", StringCodec.INSTANCE);
        Mono<String> name = map.put("name", "sam");
        Mono<String> age = map.put("age", "19");
        Mono<String> city = map.put("city", "Medellin");

        StepVerifier.create(name.concatWith(age).concatWith(city).then())
                .verifyComplete();
    }

    @Test
    void mapTest2() {
        RMapReactive<String, String> map = this.redissonReactiveClient.getMap("user:2", StringCodec.INSTANCE);
        Map<String, String> javaMap = Map.of(
                "name", "jake",
                "age", "22",
                "city", "Boston"
        );
        StepVerifier.create(map.putAll(javaMap).then())
                .verifyComplete();
    }

    @Test
    void mapTest3() {
        //Map<Integer,Student>
        TypedJsonJacksonCodec codec = new TypedJsonJacksonCodec(Integer.class, Student.class);
        RMapReactive<Integer, Student> map = this.redissonReactiveClient.getMap("users",codec);

        Student student1= new Student("sam",20,"Atalanta", List.of(1,2,3));
        Student student2= new Student("jake",30,"Miami", List.of(10,20,30));

        Mono<Student> mono1=map.put(1,student1);
        Mono<Student> mono2=map.put(2,student2);

        StepVerifier.create(mono1.concatWith(mono2).then())
                .verifyComplete();

    }
}
