import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

import static java.sql.Connection.*;


class TransactionExecuter extends Thread {
    ArrayList<String> listoperations;

    public TransactionExecuter(ArrayList<String> listoperations) {
        // TODO Auto-generated constructor stub
        this.listoperations = new ArrayList<>(listoperations);
    }

    public void run() {
        System.out.println("**********Thread ID: " + this.getId());

        //Levels of isolation
//        TRANSACTION_READ_COMMITTED);
//        TRANSACTION_REPEATABLE_READ);
//        TRANSACTION_SERIALIZABLE);  //Unsure about this

        //Umcomment for postgres
        //String url = "jdbc:postgresql://localhost/tdm_low_concurrency";
        String url = "jdbc:mysql://localhost/tau_db?serverTimezone=UTC";

        //Change the user and password
        String user = "root";
        String password = "";
        postgreSQLCall(listoperations, TRANSACTION_READ_UNCOMMITTED, url, user, password);
    }

    public void postgreSQLCall(ArrayList<String> listoperations, int isolationLevel, String url, String user, String password) {

        System.out.println("\n\n");

        String operation = "";

        try {
            Connection con = DriverManager.getConnection(url, user, password);
            con.setTransactionIsolation(isolationLevel);

            Statement st = con.createStatement();
            con.setAutoCommit(false);

            //iterate through the list of operations and execute

            for (String op : listoperations) {
                operation = op.replace("\n", "").trim();
//                	System.out.println("operation= "+operation);
                if (operation.startsWith("SELECT")) {
                    ResultSet rs = st.executeQuery(operation);
                    if (rs.next()) {
                        System.out.println("SQL Query Result:  " + rs.getString(1));
                    }
                } else {
                    st.executeUpdate(operation);
                }
            }
            con.commit();
            con.close();
            st.close();
            System.out.println("Closing connection");

        } catch (SQLException ex) {
            System.out.println(operation);
            System.out.println(ex);
        }

    }
}

public class TransactionManager {
    ReaderWriter readerObj;
    int MAX_THREADS = 10;

    public TransactionManager() {
        // TODO Auto-generated constructor stub
        readerObj = new ReaderWriter("reader");
    }


    public void readTransactions() {
        try {

            ArrayList<ArrayList<String>> queue = new ArrayList<>();
            ArrayList<String> listoperations = new ArrayList<>();
            readerObj.readFromFile("TreeMap_test.txt");
            int count = 0;
            while (readerObj.getNext(listoperations).size() != 0) {
                queue.add(listoperations);
                System.out.println("***COUNT=: " + Thread.activeCount());
                while (Thread.activeCount() >= MAX_THREADS && queue.size() < 10) {
                    if (readerObj.getNext(listoperations).size() > 0)
                        queue.add(listoperations);
                    else
                        break;
                }
                if (Thread.activeCount() < MAX_THREADS && queue.size() > 0) {
                    count += queue.get(0).size();
                    System.out.println("Executing :::::" + count);
                    TransactionExecuter transaction = new TransactionExecuter(queue.remove(0));
                    transaction.start();
                }
                listoperations.clear();

            }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch  block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {
        // TODO Auto-generated method stub
        TransactionManager manager = new TransactionManager();
        manager.readTransactions();
    }

}
