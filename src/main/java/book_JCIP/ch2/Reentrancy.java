package book_JCIP.ch2;

public class Reentrancy {

}


class Widget {
    public synchronized void doSomething() {
        System.out.println(toString() + "Widget : doing ...");
    }
}

class LoggingWidget extends Widget {

    public synchronized void doSomething() {
        System.out.println(toString() + ": calling doSomething");
        super.doSomething();
    }
}
