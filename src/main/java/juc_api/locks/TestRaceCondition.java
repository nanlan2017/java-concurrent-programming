package juc_api.locks;

import static top.UtilsConcurrent.stop;
import static top.UtilsConcurrent.sleep;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;


public class TestRaceCondition {

    private int cnt;

    void increment() {
        cnt = cnt + 1;
    }

    synchronized void increment_Sync() {
        cnt = cnt + 1;
    }

    void increment_Sync_desugar() {
        synchronized (this) {
            cnt = cnt + 1;
        }
    }

    public void test_race() {
        ExecutorService executor = Executors.newFixedThreadPool(10);

        IntStream.range(0, 10000)
//                .forEach(i -> executor.submit(this::increment_Sync_desugar));
                .forEach(i -> executor.submit(this::increment_Sync_using_ReLock));

        stop(executor);

        System.out.println(cnt);
    }

    /*==========================================================================*/
    private ReentrantLock rlock1 = new ReentrantLock();

    void increment_Sync_using_ReLock() {
        rlock1.lock();
        try {
            cnt++;
        } finally {
            rlock1.unlock();
        }
    }
    /*==========================================================================*/
    public void test_ReentrantLock(){

        ExecutorService executor = Executors.newFixedThreadPool(2);
        ReentrantLock lock = new ReentrantLock();

        executor.submit(() -> {
            lock.lock();
            try {
                sleep(1);
            } finally {
                lock.unlock();
            }
        });

        executor.submit(() -> {
            System.out.println("Locked: " + lock.isLocked());
            System.out.println("Held by me: " + lock.isHeldByCurrentThread());
            boolean locked = lock.tryLock();
            System.out.println("Lock acquired: " + locked);
        });

        stop(executor);
    }

}
