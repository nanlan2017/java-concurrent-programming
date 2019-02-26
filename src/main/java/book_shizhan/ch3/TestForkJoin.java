package book_shizhan.ch3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class TestForkJoin {

    public static void test() {
        ForkJoinPool pool = new ForkJoinPool();

        //
        Fibonacci fibTask = new Fibonacci(10);
        ForkJoinTask<Integer> fibRet = pool.submit(fibTask);
        try {
            Integer res = fibRet.get();
            System.out.println("The result is : " + res);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        //
        CountTask task = new CountTask(0, 2000000L);
        ForkJoinTask<Long> ret = pool.submit(task);
        try {
            long res = ret.get();
            System.out.println("The result is : " + res);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}

class CountTask extends RecursiveTask<Long> {
    private static final int THRESHOLD = 1000;

    private long start;
    private long end;

    public CountTask(long start, long end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public Long compute() {
        long sum = 0;

        // 无需fork
        boolean canCompute = (end - start) < THRESHOLD;
        if (canCompute) {
            for (long i = start; i <= end; i++) {
                sum += i;
            }
            //
        } else {
            // 分成100个小任务
            List<CountTask> subTasks = new ArrayList<>();
            long pos = start;
            long interval = (end - start) / 100;
            for (int i = 0; i < 100; i++) {
                long last = pos + interval;
                if (last > end)
                    last = end;

                pos += (interval + 1);
                CountTask task = new CountTask(pos, last);
                subTasks.add(task);
                task.fork();   //TODO fork
            }

            for (CountTask task : subTasks) {
                sum += task.join();   //TODO join
            }
        }

        return sum;
    }
}

/**
 * =================================================================================
 */

class Fibonacci extends RecursiveTask<Integer> {
    final int n;

    Fibonacci(int n) {
        this.n = n;
    }

    public Integer compute() {
        if (n <= 1)
            return n;

        Fibonacci f1 = new Fibonacci(n - 1);
        f1.fork();

        Fibonacci f2 = new Fibonacci(n - 2);
        return f2.compute() + f1.join();
    }
}