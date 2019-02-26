package book_wwjun.pats.ch25_refs;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

public class TestWeak {
    public static void test(){
        Data data = new Data();
        WeakReference<Data> wref = new WeakReference<>(data);
        data = null;   // 则堆内存上的那个Data对象不再被任何ref引用
        System.gc();
    }

    public static void test1() throws InterruptedException {
        ReferenceQueue<Data> queue = new ReferenceQueue<>();
        Data data = new Data();

        WeakReference<Data> wref = new WeakReference<>(data,queue);  //TODO
        data = null;
        System.out.println(wref.get());

        //
        System.gc();

        TimeUnit.SECONDS.sleep(1);

        Reference<? extends Data> gc_ed_ref = queue.remove();
        System.out.println(gc_ed_ref);
    }
}
