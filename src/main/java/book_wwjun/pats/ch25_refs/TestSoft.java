package book_wwjun.pats.ch25_refs;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TestSoft {

    public static void test() throws InterruptedException {
        SoftLRUCache<Integer, Data> cache = new SoftLRUCache<>(200, key -> new Data());
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            cache.get(i);
            TimeUnit.MILLISECONDS.sleep(100);  //TODO  留下足够的时间供JVM GC尝试回收
            System.out.println("The " + i + " reference stored in cache.");
        }
    }
}

class SoftLRUCache<K, V> {

    // 链表 + Map
    private final LinkedList<K> keyList = new LinkedList<>();
    private final Map<K, SoftReference<V>> cache = new HashMap<>();   //TODO

    private final int capacity;
    private final CacheLoader<K, V> cacheLoader;

    public SoftLRUCache(int cap, CacheLoader<K, V> loader) {
        this.capacity = cap;
        this.cacheLoader = loader;
    }

    @Override
    public String toString() {
        return keyList.toString();
    }


    public void put(K key, V val) {

        if (keyList.size() >= capacity) {
            K eldestKey = keyList.removeFirst();
            cache.remove(eldestKey);
        }

        keyList.remove(key);
        keyList.addLast(key);
        cache.put(key, new SoftReference<>(val));   //TODO
    }

    // get 其实也是put
    public V get(K key) {
        V val;
        boolean success = keyList.remove(key);

        if (!success) {
            val = cacheLoader.load(key);
            put(key, val);
        } else {
            val = cache.get(key).get();   //TODO
            keyList.addLast(key);
        }
        return val;
    }
}
