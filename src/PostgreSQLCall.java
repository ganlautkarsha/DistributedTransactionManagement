import java.sql.*;

public class PostgreSQLCall {
    private Connection connect = null;
    private Statement statement = null;
    private ResultSet resultSet = null;

    public static void main(String[] args) throws Exception {
        PostgreSQLCall postgreSQLCall = new PostgreSQLCall();
        postgreSQLCall.dbInteraction();
    }

    private void dbInteraction() {
        System.out.println("\n\n");
        String url = "jdbc:postgresql:tdm_low_concurrency";
        String user = "tushar";
        String password = "tush0906";
        String query = "SELECT sen.name " +
            "FROM SENSOR sen, SENSOR_TYPE st, COVERAGE_INFRASTRUCTURE ci " +
            "WHERE sen.SENSOR_TYPE_ID=st.id AND st.name='WiFiAP' AND sen.id=ci.SENSOR_ID AND ci.INFRASTRUCTURE_ID=ANY(array['2038','3231','2019','6066','5211','3044','3066','3216','2204','4226'])";
        try {
            Connection con = DriverManager.getConnection(url, user, password);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {
                System.out.println("SQL Query Result:  " + rs.getString(1));
            }
        } catch (SQLException ex) {
            System.out.println(ex);
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
