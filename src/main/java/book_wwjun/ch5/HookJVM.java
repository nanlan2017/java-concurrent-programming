package book_wwjun.ch5;

import java.util.concurrent.TimeUnit;

public class HookJVM {
    public static void work(){
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run(){
                try {
                    System.out.println("The hook-thread-1 is running..");
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
                System.out.println("The hook-thread-1 is exit..");
            }
        });

        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run(){
                try {
                    System.out.println("The hook-thread-2 is running..");
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
                System.out.println("The hook-thread-2 is exit..");
            }
        });
    }
}
