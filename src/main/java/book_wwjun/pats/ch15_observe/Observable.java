package book_wwjun.pats.ch15_observe;

import java.util.concurrent.Callable;

public interface Observable {
    enum ExecutionStatus {
        STARTED, RUNNING, DONE, ERROR
    }

    // 屏蔽Thread的其他方法
//    void start();
//
//    void interrupt();
}

interface TaskLifecycle<T> {

    void onStart(Thread th);

    void onRunning(Thread th);

    void onFinish(Thread th, T result);

    void onError(Thread th, Exception e);

    class EmptyLifecycle<T> implements TaskLifecycle<T>{
        @Override
        public void onStart(Thread th){
            System.out.println(th.getName() + " is starting...");
        }
        @Override
        public void onRunning(Thread th){
            System.out.println(th.getName() + " is running...");
        }
        @Override
        public void onFinish(Thread th, T result){
            System.out.println(th.getName() + " is finishing...");
        }
        @Override
        public void onError(Thread th, Exception e){
            System.out.println(th.getName() + " occurs error...");
        }
    }
}

class ObservableThread<T> extends Thread implements Observable{

    private TaskLifecycle<T> cycleObserver;
    private Callable<T> task;
    private ExecutionStatus executionState;


    public ObservableThread(String name,TaskLifecycle<T> cycleObserver, Callable<T> task){
        super(name);
        this.cycleObserver = cycleObserver;
        this.task = task;
    }

    @Override
    public final void run(){
        updateAndNotify(ExecutionStatus.STARTED,null,null);
        try{
            updateAndNotify(ExecutionStatus.RUNNING, null,null);
            T result = task.call();
            updateAndNotify(ExecutionStatus.DONE,result,null);
        }catch (Exception e){
            updateAndNotify(ExecutionStatus.ERROR,null,e);
        }
    }

    private void updateAndNotify(ExecutionStatus state, T result, Exception e){
        this.executionState = state;

        switch (state){
            case STARTED:
                cycleObserver.onStart(this);
                break;
            case RUNNING:
                cycleObserver.onRunning(this);
                break;
            case DONE:
                cycleObserver.onFinish(this,result);
                break;
            case ERROR:
                cycleObserver.onError(this,e);
                break;
        }
    }

}
