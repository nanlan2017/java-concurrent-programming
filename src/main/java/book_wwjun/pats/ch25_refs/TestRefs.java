package book_wwjun.pats.ch25_refs;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TestRefs {
    public static void test1(){
        LRUCache<String, Data> cache = new LRUCache<>(5, key -> new Data());
        cache.get("A");
        cache.get("B");
        cache.get("C");
        cache.get("D");
        cache.get("E");
        //  A  < B < C < D < E
        cache.get("F");
        System.out.println(cache.toString());
    }

    public static void test2() throws InterruptedException {
        LRUCache<Integer, Data> cache = new LRUCache<>(200, key -> new Data());
        for (int i=0; i< Integer.MAX_VALUE; i++){
            cache.get(i);
            TimeUnit.MILLISECONDS.sleep(50);
            System.out.println("The "+i + " reference stored in cache.");
        }
    }
}


class LRUCache<K,V> {

    // 链表 + Map
    private final LinkedList<K> keyList = new LinkedList<>();
    private final Map<K,V> cache = new HashMap<>();

    private final int capacity;
    private final CacheLoader<K,V> cacheLoader;

    public LRUCache(int cap, CacheLoader<K,V> loader){
        this.capacity = cap;
        this.cacheLoader = loader;
    }

    @Override
    public String toString(){
        return keyList.toString();
    }

    public void put(K key, V val){
        if (keyList.size() >= capacity) {
            K eldestKey = keyList.removeFirst();
            cache.remove(eldestKey);
        }

        keyList.remove(key);
        keyList.addLast(key);
        cache.put(key,val);
    }

    // get 其实也是put (visit)
    public V get(K key){
        V val;
        boolean success = keyList.remove(key);

        if (!success){
            val = cacheLoader.load(key);
            put(key,val);
        } else {
            val = cache.get(key);
            keyList.addLast(key);
        }
        return val;
    }
}





















