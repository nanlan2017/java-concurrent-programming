package book_wwjun.ch14_singleton;

import java.sql.Connection;

/**
 * 综合考虑： 1. 线程安全 2.高性能  3.懒加载
 */
public class Singletons {
}

/**
 * 线程安全、但不是懒加载
 */
class S1 {
    private static S1 instance = new S1();

    private S1() {
    }

    public static S1 getInstance() {
        return instance;
    }
}

/**
 * 非线程安全 （check-then-action 都是非线程安全、会发生Race Condition的！）
 */
class S2 {
    private static S2 instance;

    private S2() {
    }

    public static S2 getInstance() {
        if (instance == null) instance = new S2();
        return instance;
    }
}

class S3 {
    private static S3 instance;

    private S3() {
    }

    public static synchronized S3 getInstance() {
        if (instance == null) instance = new S3();
        return instance;
    }
}

/**
 * Double-check :
 */
class S4 {
    private static S4 instance = null;

    Connection conn;

    private S4() {
    }

    public static S4 getInstance() {
        if (instance == null) {
            synchronized (S4.class) {
                if (instance == null)
                    instance = new S4();
            }
        }
        return instance;
    }
}

class S5 {
    private volatile static S5 instance = null;   // 加volatile，保证instance先于conn完成初始化
    Connection conn;  // ？

    private S5() {
    }

    public static S5 getInstance() {
        if (instance == null) {
            synchronized (S5.class) {
                if (instance == null)
                    instance = new S5();
            }
        }
        return instance;
    }
}

/**
 * Holder 方式（ 最为高明）
 */
final class S6 {
    private S6() {
    }

    public static S6 getInstance() {
        return Holder.instance;
    }

    private static class Holder {
        private static S6 instance = new S6();
    }
}