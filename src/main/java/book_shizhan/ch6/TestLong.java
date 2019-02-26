package book_shizhan.ch6;

import java.util.Random;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.stream.IntStream;

public class TestLong {
    public static void testLong() {
        LongAccumulator acc = new LongAccumulator(Long::max, 0L);

        Thread[] ths = new Thread[1000];

        IntStream.range(0, 1000).forEach(i -> {
            ths[i] = new Thread(() -> {
                Random random = new Random(10);
                long val = random.nextLong();
                acc.accumulate(val);
            });
            ths[i].start();
        });

        try {
            for (int i = 0; i < 1000; i++) {
                ths[i].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(acc.longValue());
    }
}
