package multi_threads;



public class Monitor {
    public static void test_monitor() {

        ExeThread test_th = new ExeThread();
        test_th.start();

        try {
            synchronized (test_th) {
                Thread.sleep(4000);
                System.out.println("wake the thread in main...");
                test_th.isFire = true;
                test_th.notify();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class ExeThread extends Thread {

    /**
     * 线程锁
     */
//    private final Object object = new Object();

    volatile boolean isFire = false;

    @Override
    public void run() {
        System.out.println("开始执行线程。。。");
        System.out.println("进入等待状态。。。");
        synchronized (this) {
            try {
                while (!isFire) {
                    wait(3000);        // 将本线程加入 本object 的wait set (而非monitor set)
                }
            } catch (InterruptedException e) {
                System.out.println("在" + hashCode() +"这个对象内置锁的wait-set中wait超过3s，已中断。。。");
                e.printStackTrace();
            }
        }
        System.out.println("线程结束。。。");
    }
}
