package book_wwjun.pats.ch21_thlocal;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class TestThreadLocal {
    public static void showcase1(){
        ThreadLocal<Integer> th_local_int = new ThreadLocal<>();

        IntStream.range(0,10).forEach(i-> new Thread(()->
        {
            th_local_int.set(i);
            System.out.println(Thread.currentThread() + " set i " + th_local_int.get());

            // sleep for 1s
            try{
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e){
                e.printStackTrace();
            }

            System.out.println(Thread.currentThread() + " get i " + th_local_int.get());
        }).start());
    }

    public static void showcase2() throws InterruptedException {
        ThreadLocal<Integer> th_local_int = ThreadLocal.withInitial(()->0);  // 初始值

        // debug看
        Thread testTh = new Thread(()->
        {
            th_local_int.set(99);   // 相当于 currentThread.threadLocalMap ===> set(the_local_int, 99)
            th_local_int.get();     // 相当于 currentThread.threadLocalMap ===> get(the_local_int)

            System.out.println(th_local_int.get());
        });
        testTh.start();  // 此时才会运行到内部的代码、才设置threadlocal啊！
        testTh.join();

        System.out.println(000);
    }
}
