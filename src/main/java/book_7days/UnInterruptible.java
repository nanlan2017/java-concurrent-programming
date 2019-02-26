package book_7days;

import static top.MyUtils.*;

public class UnInterruptible {



    public static void work() throws InterruptedException {


        final Object o1 = new Object();
        final Object o2 = new Object();

        Thread t1 = new Thread() {
            public void run() {
                try {
                    WAIT("t1", "o1");
                    synchronized (o1) {
                        GET("t1", "o1");

                        Thread.sleep(1000);
                        WAIT("t1", "o2");
                        synchronized (o2) {
                            GET("t1", "o2");
                        }
                        LEAVE("t1", "o2");
                    }
                    LEAVE("t1", "o1");
                } catch (InterruptedException e) {
                    System.out.println("t1 : interrupted.");
                }
            }
        };


        Thread t2 = new Thread() {
            public void run() {
                try {
                    WAIT("t2", "o2");
                    synchronized (o2) {
                        GET("t2", "o2");

                        Thread.sleep(1000);
                        WAIT("t2", "o1");
                        synchronized (o1) {
                            GET("t2", "o1");
                        }
                        LEAVE("t2", "o1");
                    }
                    LEAVE("t2", "o2");
                } catch (InterruptedException e) {
                    System.out.println("t2 : interrupted.");
                }
            }
        };

        t1.start();
        t2.start();

        Thread.sleep(2000);

        t1.interrupt();
        t2.interrupt();

        t1.join();
        t2.join();
    }
}
