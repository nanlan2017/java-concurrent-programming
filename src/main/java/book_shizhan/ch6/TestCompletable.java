package book_shizhan.ch6;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static top.MyUtils.sleepForSeconds;

public class TestCompletable {

    public static void test(){
        CompletableFuture<Integer> comFuture = new CompletableFuture<>();
        new Thread(new AskTask(comFuture)).start();

        sleepForSeconds(2);
        comFuture.complete(60);   // 2s 后才为该future设置结果值

        //TODO     CompletableFuture 实现了 CompletionStage接口（可以进行CPS-链式运算）
        comFuture.thenApply(x -> x*3)
                .thenAccept(System.out::println)
                .thenRun(()->System.out.println("End."));
    }

    public static void test2(){
        CompletableFuture<Integer> comFuture = CompletableFuture.supplyAsync(()->{
            sleepForSeconds(3);
            return 20;
        });

        try {
            System.out.println(comFuture.get());       // 在此会一直阻塞，知道能get到结果值
        } catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
        }

    }
}

class AskTask implements Runnable{

    private CompletableFuture<Integer> ret;

    public AskTask(CompletableFuture<Integer> r){
        ret = r;
    }

    @Override
    public void run(){
        int myRe = 0;
        try {
            myRe = ret.get() * ret.get();        // 在此会一直阻塞，知道能get到结果值
        } catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
        }
        System.out.println(myRe);
    }
}
