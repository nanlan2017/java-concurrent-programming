package book_wwjun.ch8;

/**
 * 线程池的总体工作：
 *      tasks被维护在一个队列中。任务多时，就新加一些线程；任务少时，就回收一些线程。
 *
 * 1. 任务队列
 * 2. 线程数量管理： init <= core <= max
 * 3. 任务拒绝策略（线程数量已达上限且任务队列已满，需要提醒任务提交者）
 * 4. 线程工厂：用于产生线程
 * 5. QUEUE_SIZE
 * 6. ALIVE_INTERNAL
 */
public interface ThreadPool {
    void execute(Runnable runnable);
    void shutdown();

    // getSize
    int getInitSize();
    int getCoreSize();
    int getMaxSize();

    int getQueueSize();
    int getAliveCount();
    boolean isShutDown();
}


/**
 * 任务队列
 */
interface RunnableQueue{
    void offer(Runnable runnable);
    Runnable take();
    int size();
}

/**
 * 线程工厂
 */
@FunctionalInterface
interface ThreadFactory{
    Thread createThread(Runnable runnable);
}

class RunnableDenyException extends RuntimeException{
    public RunnableDenyException(String msg){
        super(msg);
    }
}

@FunctionalInterface
interface DenyPolicy{
    void reject(Runnable runnable,ThreadPool pool);

    class DiscardDenyPolicy implements DenyPolicy{
        @Override
        public void reject(Runnable runnable,ThreadPool pool){
            // do nothing
        }
    }

    class AbortDenyPolicy implements DenyPolicy{
        @Override
        public void reject(Runnable runnable,ThreadPool pool){
            throw new RunnableDenyException("The runnable "+runnable + " will be aborted.");
        }
    }

    class RunnerDenyPolicy implements DenyPolicy{
        @Override
        public void reject(Runnable runnable,ThreadPool pool){
            if (!pool.isShutDown())
                runnable.run();
        }
    }
}