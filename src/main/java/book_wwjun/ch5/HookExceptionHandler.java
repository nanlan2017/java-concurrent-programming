package book_wwjun.ch5;

import java.util.concurrent.TimeUnit;

public class HookExceptionHandler {
    public static void work(){
        Thread.setDefaultUncaughtExceptionHandler((th,e)->
        {
            System.out.println("Caught exception in thread : "+ Thread.currentThread().getName());
            System.out.println(th.getName() + " occurs exception!");
            e.printStackTrace();
        });

        final Thread th = new Thread(()->
        {
            try{
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e){

            }

            System.out.println(1/0);
        },"TestCh15-thread");
        th.start();
    }
}
