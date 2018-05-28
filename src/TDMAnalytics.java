public class TDMAnalytics {
    int threadCount = 0;

    public static float totalResponseTimeforRead;
    public static int totalReads;
    public static float totalWorkloadResponseTime;
    public static int totalWorkload;
    public static float totalExecutionTime;
    public static int totalTransactions;
    public static int totalRows;
    public static final Object lock = new Object();

//    ******** Constructor ********
    public TDMAnalytics(int threadCount) {
        this.threadCount = threadCount;
    }

//    ******** Analytics Calculations **********
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

//        **************** Average Workload Response Time Calculations ***********************
        float averageRows;
        averageRows = totalRows / totalReads;
        String averageRowsinString = String.format("%.8f", averageRows);
        System.out.println("Average Rows Returned: " + averageRowsinString);

    }
}