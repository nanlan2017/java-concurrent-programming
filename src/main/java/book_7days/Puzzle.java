package book_7days;

public class Puzzle {

    static boolean flag = false;
    static int ans = 0;

    static Thread th1 = new Thread() {
        public void run() {
            System.out.println("———————————— Running th1... ——————————————");
            ans = 42;
            flag = true;
            System.out.println("———————————— flag has been setted! ——————————————");
        }
    };

    static final Thread th2 = new Thread(() -> {

//            if (flag){
//                System.out.println("The meaning of life is :" + ans);
//            } else {
//                System.out.println("No Answer");
//            }

        while (!flag) {
            System.out.println("going to sleep...");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("The meaning of life is :" + ans);
    });

    public static void work() throws InterruptedException {
        th2.run();  // 为什么不会被调度啊？？
        th1.run();

        System.out.println("———————————— th1，th2 has been fired ... ——————————————");

        th1.join();
        th2.join();
    }
}
