import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


public class ThreadPool
{
//    ReaderWriter readerObj;
    int select_count = 0;
    static ThreadPoolExecutor executor;
    TransactionManager manager;

    public ThreadPool () {
//        readerObj = new ReaderWriter("reader");
        manager = new TransactionManager();
    }

    private void readTransactions() throws IOException, ClassNotFoundException {
//        ArrayList<ArrayList<String>> queue = new ArrayList<>();
        ArrayList<String> listOfOperations = new ArrayList<>();
        manager.readTransactions();
        System.out.println("Operation Map: " + TransactionManager.operationMap.get(0));
        TransactionManager.threadQueue.add(TransactionManager.operationMap.get(0));
        System.out.println("Thread Queue Size: " + TransactionManager.threadQueue.size());
        int count=0;
        while((listOfOperations = manager.getNext()).size()!=0) {
            count++;
//            System.out.print(count + ' ');
            System.out.println("Active Threads: " + executor.getActiveCount());
            System.out.println("listofOperations in readTransactions: " + listOfOperations.size());
            Task task = new Task(listOfOperations);
            executor.execute(task);
            System.out.println("Active Threads: " + executor.getActiveCount());
        }
        System.out.println(count);
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        int threadCount = 2;

        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadCount);

        ThreadPool threadExecutor = new ThreadPool();
        threadExecutor.readTransactions();

        System.out.println("Maximum threads inside pool " + executor.getMaximumPoolSize());
        executor.shutdown();
    }
}