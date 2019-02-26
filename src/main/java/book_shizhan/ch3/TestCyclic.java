package book_shizhan.ch3;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import static top.MyUtils.sleepForSeconds;

/**
 * 士兵报到（10个算一批）
 */
public class TestCyclic {
    public static void test() throws InterruptedException{
        final int N = 10;

        Thread[] allSoldiers = new Thread[N*2];

        // 10个一等待，一批到齐后走 BarrierTask.run()
        CyclicBarrier barrier = new CyclicBarrier(N,()->System.out.println("------已报到一批次（10个）-----"));

        System.out.println("集合队伍 >>>>>>");

        for(int i=0; i < N*2; i++){
            System.out.println("士兵 "+ i + "报到！");
            allSoldiers[i] = new Thread(new SoldierTask(barrier,"士兵"+i));
            allSoldiers[i].start();
        }
    }
}

class SoldierTask implements Runnable{
    private String sname;
    private final CyclicBarrier barrier;

    public SoldierTask(CyclicBarrier b, String name){
        sname = name;
        barrier = b;
    }

    @Override
    public void run(){
        try{
            barrier.await();
            sleepForSeconds(3);
            System.out.println(sname + " : 任务完成");
            barrier.await();

        }catch (BrokenBarrierException | InterruptedException e){
            e.printStackTrace();
        }
    }
}

