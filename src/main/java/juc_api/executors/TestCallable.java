package juc_api.executors;

import java.util.concurrent.*;

/**
 * In addition to Runnable executors support another kind of task named Callable.
 * Callables are functional interfaces just like runnables but instead of being void they return a value.
 * 【Callable的 task 和 Runnable的 task 区别就在于 callable 是有返回值的！ 】
 * 【但是把 callable<RetT>  task 提交给executor后，由于该线程并不会被立即执行，所以无法立即获取 返回值】
 * ████████ 所以，用 Future<RetT> 来代替那个返回值，就如同惰性求值中的 @thunk一样！
 */
public class TestCallable {

    public static void work() {

        Callable<Integer> task = () -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                return 123;
            } catch (InterruptedException e) {
                throw new IllegalStateException("task interrupted", e);
            }
        };

        ExecutorService executor = Executors.newFixedThreadPool(1);
        Future<Integer> future = executor.submit(task);

        System.out.println("future done? " + future.isDone());

        try {
            Integer result = future.get();

            System.out.println("future done? " + future.isDone());
            System.out.println("result: " + result);
        } catch (InterruptedException | ExecutionException ee) {
            ee.printStackTrace();
        }
    }

    /*============================= 3s 的超时Future ===========================*/
    public static void work2() {

        Callable<Integer> task = () -> {
            try {
                TimeUnit.SECONDS.sleep(3);     // 3s 后才能得到结果
                return 123;
            } catch (InterruptedException e) {
                throw new IllegalStateException("task interrupted", e);
            }
        };

        ExecutorService executor = Executors.newFixedThreadPool(1);
        Future<Integer> future = executor.submit(task);

        System.out.println("future done? " + future.isDone());

        try {
            Integer result = future.get(1, TimeUnit.SECONDS);  // 等待超过1s 就抛 TimeOutException

            System.out.println("future done? " + future.isDone());
            System.out.println("result: " + result);
        } catch (InterruptedException | ExecutionException | TimeoutException ee) {
            ee.printStackTrace();
        }
    }
}
