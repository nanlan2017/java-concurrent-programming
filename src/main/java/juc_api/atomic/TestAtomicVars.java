package juc_api.atomic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static top.UtilsConcurrent.stop;

public class TestAtomicVars {
    public static void test_AtomicInteger(){
        AtomicInteger atomicInt = new AtomicInteger(0);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        IntStream.range(0, 100)
                .forEach(i -> {
                    Runnable task = () ->
                            atomicInt.accumulateAndGet(i, (n, m) -> n + m);
                    executor.submit(task);
                });

        stop(executor);

        System.out.println(atomicInt.get());    // => 499500
    }
}
