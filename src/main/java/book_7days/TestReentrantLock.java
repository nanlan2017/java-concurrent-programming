package book_7days;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class TestReentrantLock {

}

/**
 * 内置的锁 synchronized(obj) {...} 无法通过th.interrupt() 终止。
 *
 * 而 ReentrantLock 可以通过 th.interrupt() 中止，
 * 而且，可以设置等待 lock的超时时间 （tryLock(t)）
 */
class Person extends Thread {
    private ReentrantLock leftLock, rightLock;
    private Random random;

    // 构造时绑定两把锁
    public Person(ReentrantLock lok1, ReentrantLock lok2){
        this.leftLock = lok1;
        this.rightLock = lok2;
        random = new Random();
    }

    public void run(){
    }
}
