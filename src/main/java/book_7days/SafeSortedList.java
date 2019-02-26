package book_7days;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 核心问题： 如何让多个线程的 链表插入操作 是安全的？
 * 解决方案： 定位到需插入在 a,b 两个节点中间时，必须先获取 a,b的 lock， 然后才能插入其中
 */
class Node {
    int val;
    Node prev, next;
    ReentrantLock lok = new ReentrantLock();

    Node() {}
    Node(int v,Node p,Node n) {
        val = v;
        prev = p;
        next = n;
    }
}

// 已排序。现考虑有多个插入操作在同时进行
public class SafeSortedList {
    private Node head , tail;

    //  h ~ t
    public SafeSortedList(){
        head = new Node();
        tail = new Node();
        head.next = tail;
        tail.prev = head;
    }

    public void insert(int val){
        Node cur_node = head;
        Node next_node = cur_node.next;

        cur_node.lok.lock();
        try {
            while (true){
                next_node.lok.lock();

                try{
                    // 大 ~ 小
                    if (next_node==tail || next_node.val < val) {
                        Node new_node = new Node(val,cur_node,next_node);
                        next_node.prev = new_node;
                        cur_node.next = new_node;
                        return;
                    }
                } finally {
                    cur_node.lok.unlock();
                }

                cur_node = next_node;
                next_node = cur_node.next;


            }
        } finally {
            next_node.lok.unlock();
        }


    }

    public int size(){
        Node cur_node = tail;
        int count = 0;

        while (cur_node.prev != head) {
            ReentrantLock lok = cur_node.lok;
            lok.lock();
            try{
                ++count;
                cur_node = cur_node.prev;
            }finally {
                lok.unlock();
            }
        }
        return count;
    }

}
