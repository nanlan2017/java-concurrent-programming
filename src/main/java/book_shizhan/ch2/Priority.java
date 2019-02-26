package book_shizhan.ch2;

class High extends Thread {
    static int i = 0;

    @Override
    public void run(){
        System.out.println(System.currentTimeMillis());
        while(true){
            synchronized (Priority.class){
                i++;
                if (i > 10000){
                    System.out.println("High has completed.");
                    break;
                }
            }
        }
    }
}

class Low extends Thread {
    static int i = 0;

    @Override
    public void run(){
        while(true){
            synchronized (Priority.class){
                i++;
                if (i > 10000){
                    System.out.println("Low has completed.");
                    break;
                }
            }
        }
    }
}


public class Priority {
    public static void test(){
        //TODO 会发现高优先级的线程一般都是先跑完（分配的timeslice多）
        Thread high_th = new High();
        Thread low_th = new Low();
        high_th.setPriority(Thread.MAX_PRIORITY);
        low_th.setPriority(Thread.MIN_PRIORITY);
        high_th.start();
        low_th.start();
    }
}
