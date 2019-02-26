package book_JCIP.ch3;

public class NoVisibility {
    private static boolean ready;
    private static int number;

    private static class ReaderThread extends Thread {
        public void run() {
            while (!ready)
                Thread.yield();   // yield : 挂起当前线程、切换回主线程？
            System.out.println(number);
        }
    }

    public static void run() {
        new ReaderThread().start();
        number = 42;
        ready = true;
    }
}
