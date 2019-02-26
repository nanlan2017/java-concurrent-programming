package book_shizhan.ch8_debug;

import java.util.ArrayList;

public class UnsafeArrayList {
    static ArrayList<Object> alist = new ArrayList<>();

    static class AddTask implements Runnable{
        @Override
        public void run(){
            try{
                Thread.sleep(100);
            }catch (InterruptedException e){
                // do nothing
            }

            for(int i = 0; i< 1000000; i++){
                alist.add(new Object());
            }
        }
    }

    public static void test() {
        Thread t1 = new Thread(new AddTask(),"t1");
        Thread t2 = new Thread(new AddTask(),"t2");

        t1.start();
        t2.start();

        Thread t3 = new Thread(()->{
            while(true){
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    // do nothing
                }
            }
        },"t3");

        t3.start();
    }
}
