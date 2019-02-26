package juc_api.executors;

public class TestRunnable {

    public static void work(){

        Runnable task = () -> {
            String threadName = Thread.currentThread().getName();
            System.out.println("Hello " + threadName);
        };

        task.run();  // 竟然是 Hello main ,看来 Runnable并非一个线程？？ 而只是由调用的线程在运行？？

        Thread thread = new Thread(task);
        thread.start();

        System.out.println("Done!");
    }
}
