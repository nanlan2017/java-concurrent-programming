package book_wwjun.pats.ch19_future;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class TestCh19 {

    /**
     *  测试： side-effect only型任务
     */
    public static void test1() {
        Executor<Void, Void> service = new ExecutorImpl<>();

        Future<?> future = service.submit(() ->
        {
            System.out.println("runnable finished.");
        });

        try {
            future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     *  测试： in -> out 型任务
     */
    public static void test2() {
        Executor<String, Integer> service = new ExecutorImpl<>();

        Future<Integer> future = service.submit(str->
        {
            try{
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
            return str.length();
        }, "wangjiaheng");

        try {
            Integer result = future.get();    // 此处会进入阻塞5s
            System.out.println("The result is : "+ result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     *  测试： 支持callback
     */
    public static void test3() {
        Executor<String, Integer> service = new ExecutorImpl<>();

        service.submit(str->
        {
            try{
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
            return str.length();
        }, "wangjiaheng", System.out::println);  // consumer: 打印这个结果length
    }
}

/**
 * 一个能够获取到T值的对象
 */
interface Future<T> {
    T get() throws InterruptedException;

    boolean isDone();
}

@FunctionalInterface
interface Task<Tin, Tout> {
    Tout get(Tin input);
}

interface Executor<Tin, Tout> {
    Future<?> submit(Runnable runnable);
    Future<Tout> submit(Task<Tin, Tout> task, Tin input);
    void submit(Task<Tin, Tout> task, Tin input, Consumer<Tout> callback);
}

class ExecutorImpl<Tin, Tout> implements Executor<Tin, Tout> {

    @Override
    public Future<?> submit(Runnable runnable) {
        final FutureTask<Void> future = new FutureTask<>();

        new Thread(() ->
        {
            runnable.run();
            future.setResult(null);
        }).start();

        return future;
    }

    @Override
    public Future<Tout> submit(Task<Tin, Tout> task, Tin input) {
        final FutureTask<Tout> future = new FutureTask<>();

        new Thread(() ->
        {
            Tout result = task.get(input);   // ★
            future.setResult(result);
        }).start();

        return future;
    }

    @Override
    public void submit(Task<Tin, Tout> task, Tin input, Consumer<Tout> callback){
        final FutureTask<Tout> future = new FutureTask<>();

        new Thread(() ->
        {
            Tout result = task.get(input);
            future.setResult(result);
        }).start();

        try {
            callback.accept(future.get());  // ★
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class FutureTask<T> implements Future<T> {

    private T result;
    private boolean isDone = false;


    @Override
    public T get() throws InterruptedException {

        synchronized (this) {
            while (!isDone) {
                this.wait();
            }
            return result;
        }
    }

    @Override
    public boolean isDone() {
        return isDone;
    }

    protected void setResult(T result) {
        synchronized (this) {
            if (isDone) return;

            this.result = result;
            this.isDone = true;
            this.notifyAll();
        }
    }
}

