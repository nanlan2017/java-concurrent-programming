package book_wwjun.ch12;

import java.util.concurrent.TimeUnit;

public class VolatileTest {
    final static int MAX = 5;
    static volatile int init_val = 0;

    public static void test() {
        // Reader
        new Thread(() -> {
            int localval = init_val;

            while (localval < MAX) {
                if (localval != init_val) {
                    System.out.println("The init-val is updated to : " + init_val);
                    localval = init_val;
                }
            }

            System.out.println("end-of:Reader");
        }, "Reader").start();

        // Writer
        new Thread(() -> {
            int localval = init_val;

            while (localval < MAX) {

                System.out.println("The init-val will be changed to : " + ++localval);
                init_val = localval;
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("end-of:Writer");
        }, "Writer").start();
    }
}
