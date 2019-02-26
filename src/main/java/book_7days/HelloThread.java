package book_7days;

public class HelloThread {

    public static void work() throws InterruptedException {
        Thread my_th = new Thread(){
            public void run() {
                System.out.println("Hello from my_th");
            }
        };
        my_th.start();
        Thread.yield();
//        Thread.sleep(1);
        System.out.println("Hello from Main-thread");
        my_th.join();
    }

    /*===================================================================================*/

    public static void work2() throws InterruptedException {

        class Counter {
            private int count = 0;

            /**
             * “竞态条件” 是指那么一种情形。 比如 read-modify-write 的两个线程交错了。
             *  ————— 两个线程将访问同一数据，而它们先后的时间将导致结果不同。———— 这种情形都算“竞态条件”
             *  ====>  解决方案：将那一段会被互相交错的操作“原子化”，可通过 synchronized block (使用的锁为该Counter对象的内置锁)
             *  ██████ 就记着 synchronized block 就如同transaction一样就行了，只会被一个线程一次执行完。
             *
             *
             * ██████ 所有对象都可以作为synchronized绑定的锁（这就是”内置锁“）。而且其影响与该对象本身无关！！（它只是代表一个锁而已）
             *  synchronized method1 = { ...block...}
             *  === synchronized (this) { ...block....}
             *  === wait(this-mutex) ; {...block...} ; signal(this-mutex)
             */
//            synchronized
            void increment() {++count;}
            private int getCount() {return count;}
        }

        final Counter counter = new Counter();

        class CountingThread extends Thread {
            public void run(){
                for (int i = 0; i < 10000; ++i) {
                    counter.increment();
                }
            }
        }

        CountingThread th1 = new CountingThread();
        CountingThread th2 = new CountingThread();

        th1.start();
        th2.start();

        th1.join();
        th2.join();
        System.out.println("final count: " + counter.getCount());
    }
}





