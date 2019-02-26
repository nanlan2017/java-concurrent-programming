package book_JCIP.ch1;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.NotThreadSafe;
import net.jcip.annotations.ThreadSafe;

/**
 * Shared & Mutable states (共享的可变状态 在多线程访问下需要使用同步机制来确保线程安全)
 *   -- Non-shared states : 安全
 *   -- Immutable : 安全  （不一定加了final，但final fields 一定是immutable的）
 *
 * ===============================================================================
 * 【“线程安全的Class”】
 * A class is thread-safe if it behaves correctly when accessed from multiple threads,
 * regardless of the scheduling or interleaving of the execution of those threads by the runtime environment,
 * and
 *             ★ with no additional synchronization or other coordination on the part of the calling code.
 * -- 一个thread-safe的类中封装了必要的同步机制，因此其客户端调用者无需进一步采取同步措施。
 *
 * ===============================================================================
 * 【Stateless objects are always thread-safe】
 * StatelessFactorizer is, like most servlets,
 *             ★ stateless: it has no fields and references no fields from other classes.
 * The transient state for a particular computation exists solely in local variables that are stored on the thread’s stack
 * and are accessible only to the executing thread.
 * One thread accessing a StatelessFactorizer cannot influence the result of another thread accessing the same StatelessFactorizer;
 * because the two threads do not share state, it is as if they were accessing different instances.
 */
@NotThreadSafe
public class UnsafeSequence {
    // 【Race Condition : 竞态条件】
    private int val;

    public int getNext(){ return val++ ;}
}

@ThreadSafe
class SafeSequenc{

    @GuardedBy("this")
    private int val;

    public synchronized int getNext() {return val++ ;}
}

































