package book_wwjun.ch5;

import java.util.LinkedList;

public class EventQueue {
    private final int max;
    private final static int DEFAUT_MAX_EVENT = 10;
    private final LinkedList<Event> eventQueue = new LinkedList<>();

    static class Event{

    }

    public EventQueue(){
        this(DEFAUT_MAX_EVENT);
    }
    public EventQueue(int max){
        this.max = max;
    }

    private void console(String msg){
        System.out.printf("%s:%s\n",Thread.currentThread().getName(),msg);
    }

    // 向请求队列中新增一个event
    public void offer(Event event){
        synchronized (eventQueue){
            // 检查：若已满
            while (eventQueue.size() >= max){
                try{
                    console("The eventQueue is full.");
                    eventQueue.wait();
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }

            console("The new event is submitted.");
            eventQueue.addLast(event);
            eventQueue.notifyAll();   // notify() 在该obj的wait set中的线程（而非monitor set）
        }
    }

    public Event take(){
        synchronized (eventQueue){
            // 检查：若为空
            while (eventQueue.isEmpty()){
                try{
                    console("The eventQueue is empty.");
                    eventQueue.wait();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }

            Event event = eventQueue.removeFirst();
            eventQueue.notifyAll();
            console("The event "+ event + " is handled.");
            return event;
        }
    }
}
