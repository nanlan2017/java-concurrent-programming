package book_wwjun.ch1;

import java.util.concurrent.TimeUnit;

public class TryConcurrency {
    private static void sleep(int seconds){
        try{
            TimeUnit.SECONDS.sleep(seconds);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    private static void browseNews(){
        for(;;){
            System.out.println("uh-hah, the good news.");
            sleep(1);
        }
    }

    private static void enjoyMusic(){
        for(;;){
            System.out.println("uh-hah, the nice music.");
            sleep(1);
        }
    }

    public static void work(){
        new Thread(TryConcurrency::browseNews).start();
        enjoyMusic();
    }

}
