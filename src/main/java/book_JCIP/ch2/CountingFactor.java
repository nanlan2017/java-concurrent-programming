package book_JCIP.ch2;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.NotThreadSafe;
import net.jcip.annotations.ThreadSafe;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import top.MyUtils;

/***********************************************************************/
@ThreadSafe
class CountingFactor {
    public final AtomicLong count = new AtomicLong(0);

    public BigInteger[] work(BigInteger i) {
        BigInteger[] factors = MyUtils.factor(i);
        count.incrementAndGet();
        return factors;
    }
}

@NotThreadSafe
class UnsafeCachingFactor {
    private final AtomicReference<BigInteger> lastNumber = new AtomicReference<BigInteger>();
    private final AtomicReference<BigInteger[]> lastFactors = new AtomicReference<BigInteger[]>();

    public BigInteger[] work(BigInteger i) {
        if (i.equals(lastNumber.get())) {
            return lastFactors.get();
        } else {
            BigInteger[] factors = MyUtils.factor(i);
            lastNumber.set(i);
            lastFactors.set(factors);
            return factors;
        }
    }
}

@ThreadSafe
class SafeCachingFactor {
    /**
     * ███ 每个对象都有一个“内置锁” ： 其方法的synchronized使用的就是这个锁！
     * <p>
     * 通过Factor对象的内置锁 (Intrinsic Lock/ Monitor Lock) 来保护每一个状态变量
     * 实现方式：对整个work()方法进行synchronized同步
     */

    @GuardedBy("this")
    private BigInteger lastNumber = new BigInteger("0");
    @GuardedBy("this")
    private BigInteger[] lastFactors = new BigInteger[]{};

    // synchronized  block 会以atomic 方式执行，就如同数据库中的事务一样。
    // 但是，这样的效率太低！test_submit_to_SingleThreadExecutor()只能响应一个调用者、其他的请求都会被blocked。
    public synchronized BigInteger[] work(BigInteger i) {
        if (i.equals(lastNumber)) {
            return lastFactors;
        } else {
            BigInteger[] factors = MyUtils.factor(i);
            lastNumber = i;
            lastFactors = factors;
            return factors;
        }
    }
}

/**
 * 优化上述的 SafeCachingFactor： 将synchronized被锁住的block尽量缩小
 */
class OptSafeCachingFactor {

    @GuardedBy("this")
    private BigInteger lastNumber;
    @GuardedBy("this")
    private BigInteger[] lastFactors;
    @GuardedBy("this")
    private long times_requested;
    @GuardedBy("this")
    private long times_cache_hit;

    public synchronized long get_times_requested() {
        return times_requested;
    }


    public BigInteger[] work(BigInteger i) {

        BigInteger[] factors = null;

        // 所有fields都会被锁住？
        synchronized (this) {
            ++times_requested;
            if (i.equals(lastNumber)) {
                ++times_cache_hit;
                factors = lastFactors.clone();
            }
        }

        if (factors == null) {
            factors = MyUtils.factor(i);
            synchronized (this) {
                lastNumber = i;
                lastFactors = factors.clone();
            }
        }

        return factors;
    }
}