import java.sql.*;
import java.util.ArrayList;

public class Task implements Runnable {
    private ArrayList<String> listOfOperations;
    private Connection connect = null;
    private Statement statement = null;
    private ResultSet resultSet = null;


    public Task(ArrayList<String> listOfOperations) {
        this.listOfOperations = new ArrayList<>(listOfOperations);
        System.out.println("Size of listOfOperations in Constructor: " + this.listOfOperations.size());
    }

    private void dbConnection(int isolationLevel, String url, String user, String password) throws SQLException {
        long responseTime = 0;
        int transactionReads = 0;
        long workloadResponseTime = 0;
        boolean retry;
        do {
            try {
                connect = DriverManager.getConnection(url, user, password);
                connect.setTransactionIsolation(isolationLevel);
                statement = connect.createStatement();
                connect.setAutoCommit(false);

                Timestamp workloadStartTime = new Timestamp(System.currentTimeMillis());
                for (int i = 0; i < listOfOperations.size(); i++) {
                    String op = listOfOperations.get(i);
                    String operation = op.replace("\n", "").trim();
                    if (operation.startsWith("SELECT")) {
                        Timestamp start_timestamp = new Timestamp(System.currentTimeMillis());
                        resultSet = statement.executeQuery(operation);
                        Timestamp end_timestamp = new Timestamp(System.currentTimeMillis());
                        responseTime += end_timestamp.getTime() - start_timestamp.getTime();
                        transactionReads++;
                    } else {
                        statement.executeUpdate(operation);
                    }
                }
                connect.commit();
                close();
                System.out.println("Close Connection");
                Timestamp workloadEndTime = new Timestamp(System.currentTimeMillis());
                workloadResponseTime = workloadEndTime.getTime() - workloadStartTime.getTime();
                synchronized (TDMAnalytics.lock) {
                    TDMAnalytics.totalResponseTimeforRead += responseTime;
                    TDMAnalytics.totalReads += transactionReads;
                    TDMAnalytics.totalWorkloadResponseTime += workloadResponseTime;
                    TDMAnalytics.totalWorkload += listOfOperations.size();
                }
                retry = false;
            } catch (SQLException e) {
                final String ss = e.getSQLState();
                if (ss.equals("40001") || ss.equals("40P01")) {
                    System.out.println("************** Retrying ***************");
                    retry = true;
                }
                else {
                    e.printStackTrace();
                    retry = false;
                }
            }
        } while(retry);
    }

    private void close() {
        try {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connect != null) connect.close();
        } catch (Exception e) {

        }
    }

    @Override
    public void run() {
        String url = "jdbc:postgresql://localhost/tdm_multithreading_test";
        String user = "tushar";
        String password = "tush0906";
        int isolationLevel = Connection.TRANSACTION_READ_UNCOMMITTED;
        System.out.println("Running Thread");
        try {
            dbConnection(isolationLevel, url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}