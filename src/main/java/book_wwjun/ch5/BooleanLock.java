package book_wwjun.ch5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.currentThread;

interface Lock {
    void lock() throws InterruptedException;

    void lock(long millis) throws InterruptedException, TimeoutException;

    void unlock();

    List<Thread> getLockedThreads();
}

public class BooleanLock implements Lock {
    private Thread currentThread;
    private boolean locked = false;
    private final List<Thread> blockedThreads = new ArrayList<>();

    @Override
    public void lock() throws InterruptedException {
        synchronized (this){
            while(locked){
                blockedThreads.add(currentThread());
                wait();
            }

            blockedThreads.remove(currentThread());
            locked = true;
            currentThread = currentThread();
        }
    }

    @Override
    public void lock(long millis) throws InterruptedException, TimeoutException {
        synchronized (this){
            if (millis <= 0){
                lock();
            } else {
                long remainingMills = millis;
                long endMills = currentTimeMillis() + remainingMills;
                while (locked){
                    if (remainingMills <= 0)
                        throw new TimeoutException("Can't get the lock during "+millis);
                    if (!blockedThreads.contains(currentThread()))
                        blockedThreads.add(currentThread());

                    wait(remainingMills);
                    remainingMills = endMills - currentTimeMillis();

                }
                blockedThreads.remove(currentThread());
                locked = true;
                currentThread = currentThread();
            }
        }
    }


    @Override
    public void unlock(){
        synchronized (this){
            if (currentThread == currentThread()){
                locked = false;
                Optional.of(currentThread().getName() + " released the lock.").ifPresent(System.out::println);
                notifyAll();
            }
        }
    }

    @Override
    public List<Thread> getLockedThreads(){
        return Collections.unmodifiableList(blockedThreads);
    }
}
