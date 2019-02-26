package book_wwjun.pats.ch25_refs;


public class Data {
    // 1 M
    private final byte[] data = new byte[2 << 19];

    @Override
    protected void finalize() throws Throwable{
        System.out.println("——————————The data will be GC.——————————");
    }
}

@FunctionalInterface
interface CacheLoader<K,V>{
    V load(K key);
}
