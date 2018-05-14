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
    static int threadCount = 10;

    public ThreadPool () {
//        readerObj = new ReaderWriter("reader");
        manager = new TransactionManager();
    }

    private void populateMap () throws IOException, ClassNotFoundException {
        manager.readTransactions();
    }

    private void statsCalculations(Timestamp startTime, Timestamp endTime, int numberOfTransactions) {
        long milliseconds = endTime.getTime() - startTime.getTime();
        int seconds = (int) milliseconds / 1000;
        TDMAnalytics.MPL_list.add(this.threadCount);
        float throughput = (float)numberOfTransactions/seconds;
        TDMAnalytics.throughput.add(throughput);
        System.out.println("Throughput: " + throughput + " MPL: " + threadCount);
    }

    private void readTransactions() throws IOException, ClassNotFoundException, InterruptedException {
        Timestamp start_timestamp = new Timestamp(System.currentTimeMillis());
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
//            System.out.print(count + ' ');
//            System.out.println("Active Threads: " + executor.getActiveCount());
//            System.out.println("listofOperations in readTransactions: " + listOfOperations.size());
            Task task = new Task(listOfOperations);
            executor.execute(task);
            count++;
//            System.out.println("Active Threads: " + executor.getActiveCount());
        }
        while((listOfOperations = manager.getNext()).size()>0) {
            Task task = new Task(listOfOperations);
            executor.execute(task);
            count++;
        }
        System.out.println("*********************************************************************************");
        Timestamp end_timestamp = new Timestamp(System.currentTimeMillis());
        statsCalculations(start_timestamp, end_timestamp, count);
        System.out.println(count);
//        System.out.println("Maximum threads inside pool " + executor.getMaximumPoolSize());
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadCount);
        ThreadPool threadExecutor = new ThreadPool();
        threadExecutor.populateMap();
        Timer timerObject = new Timer();
        timerObject.schedule(new OperationScheduler(timerObject), 0, 10);
        threadExecutor.readTransactions();
//        System.out.println("Active Threads before shutdown: " + executor.getActiveCount());
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
//        System.out.println("Active Threads after shutdown: " + executor.getActiveCount());
        System.out.println("Finished all threads");
        System.exit(0);
    }
}