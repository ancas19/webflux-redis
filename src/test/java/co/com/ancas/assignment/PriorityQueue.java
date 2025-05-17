package co.com.ancas.assignment;

import org.redisson.api.RScoredSortedSetReactive;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class PriorityQueue {

    private RScoredSortedSetReactive<UserOrder> queue;

    public PriorityQueue(RScoredSortedSetReactive<UserOrder> queue) {
        this.queue = queue;
    }

    public Mono<Void> addOrder(UserOrder order) {
        return this.queue.add(
                getScore(order.getCategory()),
                order
        ).then();
    }

    public Flux<UserOrder> takeItems() {
        return this.queue.takeFirstElements()
                .limitRate(1);
    }


    private  double getScore(Category category){
        return category.ordinal()+Double.parseDouble("0."+System.nanoTime());
    }
}
