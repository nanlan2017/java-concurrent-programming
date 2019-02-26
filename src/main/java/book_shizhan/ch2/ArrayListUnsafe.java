package book_shizhan.ch2;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class ArrayListUnsafe {

    static ArrayList<Integer> ilist = new ArrayList<>(10);

    static class AddTask implements Runnable{
        @Override
        public void run(){
            IntStream.range(0,10000).forEach(i -> ilist.add(i));
        }
    }

    public static void test() throws InterruptedException {
        Thread th1 = new Thread(new AddTask());
        Thread th2 = new Thread(new AddTask());
        th1.start();
        th2.start();
        th1.join();
        th2.join();
        System.out.println(ilist.size());
    }
}
