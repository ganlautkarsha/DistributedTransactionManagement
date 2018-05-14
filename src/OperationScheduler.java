import java.util.Timer;
import java.util.TimerTask;

class OperationScheduler extends TimerTask {
    private int i = 0;
    Timer timer = new Timer();

    public OperationScheduler(Timer timerObject) {
        System.out.println("OperationScheduler Constructor");
        this.timer = timerObject;
    }

    public void run() {
        if(TransactionManager.operationMap.get(i) == null) {
            this.timer.cancel();
            ThreadPool.flag = false;
            System.out.println("Timer Canceled");
        }
        TransactionManager.threadQueue.add(TransactionManager.operationMap.get(i));
        System.out.println("Adding: " + i);
        i++;
    }
}