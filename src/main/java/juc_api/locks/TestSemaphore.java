package juc_api.locks;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static top.UtilsConcurrent.sleep;
import static top.UtilsConcurrent.stop;

/**
 * Whereas locks usually grant exclusive access to variables or resources,
 * a semaphore is capable of maintaining whole sets of permits.
 * This is useful in different scenarios where you have to limit the amount concurrent access
 * to certain parts of your application.
 */
public class TestSemaphore {

    //TODO 一句话：允许指定数量的线程同时跑那段（而锁是只允许一个线程，exclusive的）
    public static void test_semaphore() {

        ExecutorService executor = Executors.newFixedThreadPool(10);

        Semaphore semaphore = new Semaphore(5);  // 允许5个

        Runnable longRunningTask = () -> {
            boolean permit = false;
            try {
                permit = semaphore.tryAcquire(1, TimeUnit.SECONDS);
                if (permit) {
                    System.out.println("Semaphore acquired");
//                    System.out.println("Semaphore left available permits : " + semaphore.availablePermits());
                    sleep(5);
                    System.out.println("Wake up after sleeping 5s !");
                } else {
                    System.out.println("Could not acquire semaphore");
                }
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            } finally {
                if (permit) {
                    semaphore.release();
                }
            }
        };

        // 启动10个 tasks
        IntStream.range(0, 10)
                .forEach(i -> executor.submit(longRunningTask));

        stop(executor);
    }
}
