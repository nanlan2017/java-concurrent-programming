package book_shizhan.ch3;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.IntStream;

import static top.MyUtils.sleepForSeconds;
import static top.MyUtils.timed_running;

public class TestReadWrite {
    private static Lock lock = new ReentrantLock();

    private static ReentrantReadWriteLock rwlock = new ReentrantReadWriteLock();
    private static Lock readLock = rwlock.readLock();
    private static Lock writeLock = rwlock.writeLock();

    public static void test0() {
        timed_running(TestReadWrite::test);

    }

    public static void test() {
        final Demo demo = new Demo();

        Runnable readTask = () -> {
            try {
                demo.read(readLock);
                // demo.read(lock);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        };

        Runnable writeTask = () -> {
            try {
                demo.write(readLock, new Random().nextInt(100));
                // demo.write(lock);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        };

        IntStream.range(0, 18).forEach(i -> new Thread(readTask).start());

        IntStream.range(0, 3).forEach(i -> new Thread(writeTask).start());
    }
}

class Demo {

    private int value;

    public Object read(Lock lock) throws InterruptedException {
        try {
            lock.lock();
            sleepForSeconds(1);
            return value;
        } finally {
            lock.unlock();
        }
    }

    public void write(Lock lock, int v) throws InterruptedException {
        try {
            lock.lock();
            sleepForSeconds(1);
            value = v;
        } finally {
            lock.unlock();
        }
    }
}
