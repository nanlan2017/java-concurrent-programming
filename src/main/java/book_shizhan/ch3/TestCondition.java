package book_shizhan.ch3;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static top.MyUtils.sleepForSeconds;

public class TestCondition {

    static ReentrantLock lock = new ReentrantLock();
    static Condition cond = lock.newCondition();

    public static void test(){
        new Thread(()->{
            try{
                lock.lock();
                cond.await();
                System.out.println("Thread is going on.");

            } catch (InterruptedException e){
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }).start();

        sleepForSeconds(1);
        lock.lock();
        cond.signal();  // 此时只是把 wait-set中的 thread移动到了 monitor-set，并不是直接就进入 Running了！仍要排队等获取lock
        lock.unlock();
    }
}
