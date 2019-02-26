package book_wwjun.pats.ch15_observe;

import java.util.concurrent.TimeUnit;


/**
 * 这个模式太简单了，
 * 本质上不就是一个 thread 在运行过程中不断把自己的当前status发送给 observer嘛！
 */
public class TestCh15 {

    public static void test() {

        TaskLifecycle<Integer> lifecycle =  new TaskLifecycle.EmptyLifecycle<Integer>(){
            public void onFinish(Thread th, Integer result){
                System.out.println("Thread:  "+ th.getName() + " has finished and return result :" + result);
            }
        };

        ObservableThread<Integer> th = new ObservableThread<>("Thread-Fuck",lifecycle,()->{
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
            System.out.println("______End of thread calculating!");
            return 33;
        });

        th.start();

    }
}
