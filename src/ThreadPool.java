import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


public class ThreadPool
{
    ReaderWriter readerObj;
    int select_count = 0;
    static ThreadPoolExecutor executor;

    public ThreadPool () {
        readerObj = new ReaderWriter("reader");
    }

    private void readTransactions() throws IOException, ClassNotFoundException {
//        ArrayList<ArrayList<String>> queue = new ArrayList<>();
        ArrayList<String> listOfOperations = new ArrayList<>();

        readerObj.readFromFile("TreeMap_test.txt");

        int count=0;
        while(readerObj.getNext(listOfOperations).size()!=0) {
            count++;
//            System.out.print(count + ' ');
            System.out.println(executor.getActiveCount());
//            System.out.println(listOfOperations);
            Task task = new Task(listOfOperations);
            executor.execute(task);
        }
        System.out.println(count);
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        int threadCount = 50;

        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadCount);

        ThreadPool threadExecutor = new ThreadPool();
        threadExecutor.readTransactions();

        System.out.println("Maximum threads inside pool " + executor.getMaximumPoolSize());
        executor.shutdown();
    }
}