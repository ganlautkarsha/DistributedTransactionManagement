import java.util.ArrayList;

public class TDMAnalytics {
    int threadCount = 0;

    public TDMAnalytics(int threadCount) {
        this.threadCount = threadCount;
    }

    public static int counter;
    public static ArrayList<Integer> MPL_list=new ArrayList<>();
    public static ArrayList<Float> throughput=new ArrayList<>();

    public static float totalResponseTimeforRead;
    public static int totalReads;
    public static float totalWorkloadResponseTime;
    public static int totalWorkload;
    public static float totalExecutionTime;
    public static int totalTransactions;
    public static final Object lock = new Object();

    public void statsCalculations() {

//        **************** Throughput Calculations ***********************
        float throughput = (float) (totalTransactions * 1000) / totalExecutionTime;
        System.out.println("Throughput: " + throughput + " MPL: " + this.threadCount);

//        **************** Average Read Response Time Calculations ***********************
        float averageReadResponseTime;
        averageReadResponseTime = totalResponseTimeforRead / (totalReads * 1000);
        String averageReadResponseTimeinString = String.format("%.8f", averageReadResponseTime);
        System.out.println("Average Read Response Time: " + averageReadResponseTimeinString);

//        **************** Average Workload Response Time Calculations ***********************
        float averageWorkloadResponseTime;
        averageWorkloadResponseTime = totalWorkloadResponseTime / (totalWorkload * 1000);
        String averageWorkloadResponseTimeinString = String.format("%.8f", averageWorkloadResponseTime);
        System.out.println("Average Workload Response Time: " + averageWorkloadResponseTimeinString);

    }
}