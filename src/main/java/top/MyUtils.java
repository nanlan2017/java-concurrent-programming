package top;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

public class MyUtils {

    public static BigInteger[] factor(BigInteger i){

        return new BigInteger[]{i.add(new BigInteger("1")),i.subtract(new BigInteger("1"))};
    }

    /**
     * 只接受打印String
     */
    public static void printStr(Object o){
        if (o instanceof String){
            System.out.println(o);
        } else{
            System.err.println("printStr error! this is not a string :" + o.toString());
        }
    }

    /**
     * 用于线程的log
     */
    public static void WAIT(String threadName, String lockName) {
        System.out.println(">>>>>>> " + threadName + " [ 等待  ]  " + lockName);
    }

    public static void GET(String threadName, String lockName) {
        System.out.println(">>>>>>> " + threadName + " [ 已持有 ]  " + lockName);
    }

    public static void LEAVE(String threadName, String lockName) {
        System.out.println(">>>>>>> " + threadName + " [ 已离开 ]  " + lockName);
    }

    /**
     * sleep
     */
    public static void sleepForSeconds(int time){
        try{
            TimeUnit.SECONDS.sleep(time);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public static void timed_running(Runnable runnable){
        long startTime = System.nanoTime();

        Thread th = new Thread(runnable);
        th.start();
        try {
            th.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        long endTime   = System.nanoTime();
        long time = TimeUnit.NANOSECONDS.toMillis(endTime - startTime) ;
        System.out.println("============ 运行时长：" + time + " ms ======================");
    }
}
