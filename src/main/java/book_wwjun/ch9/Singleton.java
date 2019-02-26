package book_wwjun.ch9;

public class Singleton {
    public static Singleton instance = new Singleton();

    public static int x = 0;
    public static int y;

//    public static Singleton instance = new Singleton();

    public Singleton(){
        x++;
        y++;
    }

    public static Singleton getInstance(){
        return instance;
    }

    public static void work(){
        Singleton s = Singleton.getInstance();
        System.out.println(s.x);      // x，y是被所有Singleton的实例共享的（其实是类变量）
        System.out.println(s.y);
    }
}
