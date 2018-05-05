import java.sql.*;

public class MySQLAccess {
    private Connection connect = null;
    private Statement statement = null;
    private ResultSet resultSet = null;

    public static void main(String[] args) throws Exception {
        MySQLAccess mySQLAccess = new MySQLAccess();
        mySQLAccess.readDataBase();
    }

    private void readDataBase() throws Exception {
        String tableName = "";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager
                    .getConnection("jdbc:mysql://localhost/tau_db?"
                            + "user=tau&password=tautau18&serverTimezone=UTC");
            statement = connect.createStatement();
            resultSet = statement
                    .executeQuery("select * from " + tableName);
        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }
    }


    private void close() {
        try {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connect != null) connect.close();
        } catch (Exception e) {

        }
    }
}
