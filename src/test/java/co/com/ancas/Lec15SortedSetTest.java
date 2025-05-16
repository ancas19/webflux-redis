package co.com.ancas;

import co.com.ancas.config.BaseTest;
import org.junit.jupiter.api.Test;

public class Lec15SortedSetTest extends BaseTest {

    @Test
    void sortedSet(){
        this.redissonReactiveClient.getScoredSortedSet("student:score");
    }
}
