package multi_threads;

public class ThreadStates {

    public static void work() {

        //Creating an instance of Basic Thread
        Thread th = new Thread(new BasicThread());
        System.out.println("BasicThread State: " + th.getState());   // New
        th.start();
        System.out.println("BasicThread State: " + th.getState());   // Runnable

        try {
            boolean keepRunning = true;
            int count = 1;
            while (keepRunning) {
                Thread.sleep(2000);  // 2s. 此时已交出processor，所以会调度 th ？？
                System.out.println(count * 2 + " Seconds elapsed - BasicThread State: " + th.getState()); // Runnable ??
                count++;
                if (count == 4) {
                    //6 seconds elapsed
                    synchronized (th) {
                        th.notify();    // 6s 时去唤醒 th线程
                    }
                }
                if (Thread.State.TERMINATED == th.getState()) {
                    keepRunning = false;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

class BasicThread implements Runnable {
    @Override
    public void run() {
        Thread thread = Thread.currentThread();
        try {
            //Making the thread sleep for 5 seconds
            System.out.println("Basic thread to sleep for 5 seconds");
            Thread.sleep(5000);
            synchronized (thread) {
                thread.wait();          // 5s 后开始等待notify重启
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
