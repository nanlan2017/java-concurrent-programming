package book_7days.wiki;

import java.util.HashMap;
import java.util.concurrent.*;

/*▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂*/
public class WikiFinderV2 {

    private static final HashMap<String, Integer> table = new HashMap<>();  // 唯一的全局变量
    private static final ArrayBlockingQueue<Page> pages_queue = new ArrayBlockingQueue<>(100);

    static void countWord(String word) {
        Integer n = table.get(word);
        if (n == null) {
            table.put(word, 1);
        } else {
            table.put(word, n + 1);
        }
    }

    static void countWord2(String word) {
        Integer n = table.get(word);
        if (n == null) {
            table.put(word, 1);
        } else {
            table.put(word, n + 1);
        }
    }

    private static final int NUM_CONSUMERS = 100;
    //***************************************************************************************
    public static void work(){

        Thread producer = new Thread(new Producer(pages_queue));
        Thread consumer = new Thread(new Consumer(pages_queue));

        consumer.start();
        producer.start();
        try {
            producer.join();  // 主线程等待Producer
            pages_queue.put(new Page(false));
            consumer.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    //***************************************************************************************
    public static void work2(){
        ExecutorService executor = Executors.newCachedThreadPool();
        for(int i =0 ; i< NUM_CONSUMERS ; ++i){
            executor.execute(new Consumer(pages_queue));
        }
        Thread producer = new Thread(new Producer(pages_queue));
        producer.start();
        try{
            producer.join();

            for(int i =0 ; i< NUM_CONSUMERS ; ++i){
                pages_queue.put(new Page(false));
            }

            executor.shutdown();
            executor.awaitTermination(10L, TimeUnit.MINUTES);
        } catch (Exception e){
            e.printStackTrace();
        }



    }
}
/* ▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂ */
class Producer implements Runnable {
    private BlockingQueue<Page> queue;

    public Producer(BlockingQueue<Page> q){
        queue = q;
    }

    @Override
    public void run() {
        try{
            Iterable<Page> pages = new Pages(10000, "enwiki.xml");

            for (Page page : pages) {
                queue.put(page);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

class Consumer implements Runnable {
    private BlockingQueue<Page> queue;

    public Consumer(BlockingQueue<Page> q){
        queue = q;
    }

    @Override
    public void run() {
        try{
            while (true){
                Page p = queue.take();
                if (p.isPoisonPill())
                    break;
                Iterable<String> words = new Words(p.getText());
                for (String word : words)
                    WikiFinderV2.countWord(word);
            }
        } catch (Exception e){

        }
    }
}