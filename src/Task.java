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

    private void postgreSQLConnction () {
        System.out.println("In PostgreSQLConnection");
        String url = "jdbc:postgresql://localhost/tdm_multithreading_test";
        String user = "tushar";
        String password = "tush0906";

        try {
            connect = DriverManager.getConnection(url, user, password);
            statement = connect.createStatement();
            connect.setAutoCommit(false);
            
//          *****Executing*****
//            String op: this.listOfOperations
            System.out.println("listOfOperations = " + this.listOfOperations.size());
            for(int i = 0; i < listOfOperations.size(); i++) {
                String op = listOfOperations.get(i);
                String operation = op.replace("\n", "").trim();
                if(operation.startsWith("SELECT"))
                {
                    System.out.println("SELECT Present");
                    resultSet = statement.executeQuery(operation);
                    if (resultSet.next()) {
                        System.out.println("SQL Query Result:  " + resultSet.getString(1));
                    }
                }
                else {
                    System.out.println("INSERT Present");
                    statement.executeUpdate(operation);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
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
        System.out.println("Running Thread");
        postgreSQLConnction();
    }
}