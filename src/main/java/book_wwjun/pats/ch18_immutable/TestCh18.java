package book_wwjun.pats.ch18_immutable;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class TestCh18 {
    public static void test() {
        IntegerAcc acc = new IntegerAcc(0);

        IntStream.range(0, 3).forEach(n -> new Thread(() ->
        {
            int i = 0;

            while (true) {
                int old_val;
                int res;
                synchronized (acc) {
                    old_val = acc.getVal();
                    res = acc.add(i);
                    // System.out.println(old_val + " + " + i + " = " + res);
                }
                if (i + old_val != res)
                    System.err.println("ERROR: " + old_val + " + " + i + " != " + res);
                i++;

                // sleep for 1s
                try {
                    // TimeUnit.SECONDS.sleep(1);
                    TimeUnit.MILLISECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start());
    }
}

class IntegerAcc {
    private int val;

    public IntegerAcc(int v) {
        this.val = v;
    }

    public int getVal() {
        return val;
    }

    public int add(int i) {
        val += i;
        return val;
    }
}
