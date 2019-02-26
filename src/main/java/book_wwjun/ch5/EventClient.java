package book_wwjun.ch5;

import java.util.concurrent.TimeUnit;

public class EventClient {
    public static void test(){
        final EventQueue eventQueue = new EventQueue();

        new Thread(()->{
            for (;;){
                eventQueue.offer(new EventQueue.Event());
            }
        },"Producer").start();

        new Thread(()->{
            for(;;){
                eventQueue.take();
                try{
                    TimeUnit.MILLISECONDS.sleep(10);

                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        },"Consumer").start();
    }
}
