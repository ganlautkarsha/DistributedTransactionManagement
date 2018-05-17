import java.sql.*;
import java.util.ArrayList;

public class Task implements Runnable
{
    private ArrayList<String> listOfOperations;
    private Connection connect = null;
    private Statement statement = null;
    private ResultSet resultSet = null;
    

    public Task(ArrayList<String> listOfOperations) {
        this.listOfOperations = new ArrayList<>(listOfOperations);
        System.out.println("Size of listOfOperations in Constructor: " + this.listOfOperations.size());
    }

    private void postgreSQLConnction (int isolationLevel, String url, String user, String password) throws SQLException {
//        System.out.println("In PostgreSQLConnection");
        long responseTime = 0;
        int transactionReads = 0;
        long workloadResponseTime = 0;
        try {
            connect = DriverManager.getConnection(url, user, password);
            connect.setTransactionIsolation(isolationLevel);
            statement = connect.createStatement();
            connect.setAutoCommit(false);
            
//          *****Executing*****
//            String op: this.listOfOperations
//            System.out.println("listOfOperations = " + this.listOfOperations.size());
            Timestamp workloadStartTime = new Timestamp(System.currentTimeMillis());
            for(int i = 0; i < listOfOperations.size(); i++) {
                String op = listOfOperations.get(i);
                String operation = op.replace("\n", "").trim();
                if(operation.startsWith("SELECT"))
                {
//                    System.out.println("SELECT Present");
                    Timestamp start_timestamp = new Timestamp(System.currentTimeMillis());
                    resultSet = statement.executeQuery(operation);
                    Timestamp end_timestamp = new Timestamp(System.currentTimeMillis());
                    responseTime += end_timestamp.getTime() - start_timestamp.getTime();
                    transactionReads++;
//                    if (resultSet.next()) {
//                        System.out.println("SQL Query Result:  " + resultSet.getString(1));
//                    }
                }
                else {
//                    System.out.println("INSERT Present");
                    statement.executeUpdate(operation);
                }
            }
            Timestamp workloadEndTime = new Timestamp(System.currentTimeMillis());
            workloadResponseTime = workloadEndTime.getTime() - workloadStartTime.getTime();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connect.commit();
            synchronized(TDMAnalytics.lock)
            {
                TDMAnalytics.totalResponseTimeforRead += responseTime;
                TDMAnalytics.totalReads += transactionReads;
                TDMAnalytics.totalWorkloadResponseTime += workloadResponseTime;
                TDMAnalytics.totalWorkload += listOfOperations.size();
            }
            close();
            System.out.println("Close Connection");
        }
    }

    private void close() {
        try {
            if(resultSet != null) resultSet.close();
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
            postgreSQLConnction(isolationLevel, url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}