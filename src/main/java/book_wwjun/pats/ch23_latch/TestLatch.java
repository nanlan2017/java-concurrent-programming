package book_wwjun.pats.ch23_latch;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class TestLatch {

    public static void test1() throws InterruptedException {
        Latch latch = new CountDownLatch(4);

        new PersonTravel(latch, "A", "a").start();
        new PersonTravel(latch, "B", "b").start();
        new PersonTravel(latch, "C", "c").start();
        new PersonTravel(latch, "D", "d").start();

        latch.await();   // main线程会在此阻塞，直到latch的limit = 0
        System.out.println("=========== all arrived. =================");
    }

    public static void test2() throws InterruptedException {
        Latch latch = new CountDownLatch(4);

        new PersonTravel(latch, "A", "a").start();
        new PersonTravel(latch, "B", "b").start();
        new PersonTravel(latch, "C", "c").start();
        new PersonTravel(latch, "D", "d").start();

        try {
            latch.timed_await(TimeUnit.SECONDS, 3);   // main线程会在此阻塞，直到latch的limit = 0
            System.out.println("=========== all arrived. =================");

        } catch (WaitTimeoutException e){
            // 等待超时了
            System.out.println("！！！Wait for too long...");

        }

    }
}

class WaitTimeoutException extends Exception {
    public WaitTimeoutException(String msg) {
        super(msg);
    }
}

abstract class Latch {
    protected int limit;

    public Latch(int limit) {
        this.limit = limit;
    }

    public abstract void await() throws InterruptedException;

    public abstract void timed_await(TimeUnit unit, long time) throws InterruptedException, WaitTimeoutException;

    public abstract void countDown();

    public abstract int getUnarrived();
}

class CountDownLatch extends Latch {
    public CountDownLatch(int limit) {
        super(limit);
    }

    @Override
    public void await() throws InterruptedException {
        synchronized (this) {
            while (limit > 0)
                wait();        // 只要还没都到位、就阻塞
        }
    }

    @Override
    public void timed_await(TimeUnit unit, long time) throws InterruptedException, WaitTimeoutException {

        long remainingNanos = unit.toNanos(time);
        final long endNanos = System.nanoTime() + remainingNanos;  // 截止时间点（纳秒）

        synchronized (this){
            // 一直循环！
            while (limit >0){
                if (TimeUnit.NANOSECONDS.toMillis(remainingNanos) <= 0)
                    throw new WaitTimeoutException("The wait time over given time.");

                wait(TimeUnit.NANOSECONDS.toMillis(remainingNanos));
                remainingNanos = endNanos - System.nanoTime();
            }
        }

    }

    @Override
    public void countDown() {
        synchronized (this) {
            if (limit < 0)
                throw new IllegalStateException("all of task already arrived.");

            limit--;
            notifyAll();
        }
    }

    @Override
    public int getUnarrived() {
        return limit;
    }
}

class PersonTravel extends Thread {
    private final Latch latch;
    private final String name;
    private final String transportation;

    public PersonTravel(Latch latch, String name, String transportation) {
        this.latch = latch;
        this.name = name;
        this.transportation = transportation;
    }

    @Override
    public void run() {
        System.out.println(name + ": comming on : " + transportation);

        try {
            TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(5));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(name + ": arrived by : " + transportation);

        latch.countDown();
    }
}