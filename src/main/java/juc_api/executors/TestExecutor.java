package juc_api.executors;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class TestExecutor {

    public static void test_submit_to_SingleThreadExecutor(){
        //TODO  创建一个线程池，并向其提交一个 Runnable 的task
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            String threadName = Thread.currentThread().getName();
            System.out.println("Hello " + threadName);
        });

        /**
         * 示例：如何停止 ExecutorService
         *    先shutdown()
         */
        try {
            System.out.println("attempt to shutdown executor");
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);  // 超过5s 就抛异常
        }
        catch (InterruptedException e) {
            System.err.println("tasks interrupted");
        }
        finally {
            if (!executor.isTerminated()) {
                System.err.println("cancel non-finished tasks");
            }
            executor.shutdownNow();
            System.out.println("shutdown finished");
        }
    }

    public static void test_invokeAll_with_WorkStealingPool() throws InterruptedException {
        ExecutorService executor = Executors.newWorkStealingPool();

        List<Callable<String>> callable_tasks = Arrays.asList(
                () -> "task1",
                () -> "task2",
                () -> "task3");

        executor.invokeAll(callable_tasks)
                .stream()
                .map(future -> {
                    try {
                        return future.get();
                    }
                    catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                })
                .forEach(System.out::println);
    }

    public static void test_scheduled_executor() throws InterruptedException {

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        Runnable task = () -> System.out.println("Scheduling: " + System.currentTimeMillis());
        ScheduledFuture<?> future = executor.schedule(task, 3, TimeUnit.SECONDS);

        TimeUnit.MILLISECONDS.sleep(1400);  // 3 s - 1400 ms = 1600 ms

        long remainingDelay = future.getDelay(TimeUnit.MILLISECONDS);
        System.out.printf("Remaining Delay: %sms", remainingDelay);
    }

}
