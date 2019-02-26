package book_shizhan.ch3;


/**
 * reentrantLock.lockInterruptibly()   可被中断
 * reentrantLock.tryLock()  限时等待获取锁
 */

//TODO           【Java并发模型：线程 + 锁】
//TODO 其实这些锁我都可以自己实现。真的并不难！就像那个BooleanLock一样
//TODO     因为它们实际上都是 Object.wait()/notify() 、sychorinized 这几个东西来实现的
//TODO                  无非是Thread的6个状态 & 它们处于 RunnableQueue | wait-set | monitor-set
//TODO    运行时的状态其实就是这么回事而已。包括 “阻塞”、“等待通知”.....这些场景。

public class TestReentrantLock {
}
