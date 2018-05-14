import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


public class ThreadPool
{
//    ReaderWriter readerObj;
    int select_count = 0;
    static ThreadPoolExecutor executor;
    TransactionManager manager;
    public static boolean flag = true;
    static int threadCount = 5;

    public ThreadPool () {
//        readerObj = new ReaderWriter("reader");
        manager = new TransactionManager();
    }

    private void populateMap () throws IOException, ClassNotFoundException {
        manager.readTransactions();
    }

    private void readTransactions() throws IOException, ClassNotFoundException, InterruptedException {
        long totalExecutionTime = 0;
        Timestamp startExecutionTime = new Timestamp(System.currentTimeMillis());
//        ArrayList<ArrayList<String>> queue = new ArrayList<>();
        ArrayList<String> listOfOperations = new ArrayList<>();

//        System.out.println("Operation Map: " + TransactionManager.operationMap.get(0));
//        System.out.println("Thread Queue Size: " + TransactionManager.threadQueue.size());
        int count=0;
        while(flag) {
            while((listOfOperations = manager.getNext()).size()==0) {
                System.out.println("Waiting");
                Thread.sleep(12);
            }
            System.out.println("Queue " + TransactionManager.threadQueue.size());
//            System.out.print(count + ' ');
//            System.out.println("Active Threads: " + executor.getActiveCount());
//            System.out.println("listofOperations in readTransactions: " + listOfOperations.size());
            Task task = new Task(listOfOperations);
            executor.execute(task);
            count++;
            System.out.println("Active Threads: " + executor.getActiveCount());
        }
        while((listOfOperations = manager.getNext()).size()>0) {
            Task task = new Task(listOfOperations);
            executor.execute(task);
            count++;
        }
        System.out.println("*********************************************************************************");
        Timestamp endExecutionTime = new Timestamp(System.currentTimeMillis());
        totalExecutionTime = endExecutionTime.getTime() - startExecutionTime.getTime();
        TDMAnalytics.totalExecutionTime += totalExecutionTime;
        TDMAnalytics.totalTransactions += count;
//        System.out.println("Maximum threads inside pool " + executor.getMaximumPoolSize());
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadCount);
        ThreadPool threadExecutor = new ThreadPool();
        threadExecutor.populateMap();
        Timer timerObject = new Timer();
        timerObject.schedule(new OperationScheduler(timerObject), 0, 10);
        threadExecutor.readTransactions();
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        System.out.println("Finished all threads");
        TDMAnalytics TDMAnalyticsCalculations = new TDMAnalytics(threadCount);
        TDMAnalyticsCalculations.statsCalculations();
        System.exit(0);
    }
}