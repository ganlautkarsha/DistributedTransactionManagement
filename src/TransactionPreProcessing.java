import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TransactionPreProcessing {
    List transactions;
    Map<Date, List<Transaction>> transactionGroups;

    public TransactionPreProcessing() {
        this.transactions = new ArrayList<>();
        this.transactionGroups = new HashMap<Date, List<Transaction>>();
    }

    public static void main(String[] args) throws IOException {
        TransactionPreProcessing transactionPreProcessing = new TransactionPreProcessing();
        transactionPreProcessing.generateTransactions(transactionPreProcessing);
        transactionPreProcessing.groupTransactions();
        transactionPreProcessing.printGroups();
        transactionPreProcessing.postgreSQLCall();
    }

    private void generateTransactions(TransactionPreProcessing transactionPreProcessing) throws IOException {
        String rawTransactions = transactionPreProcessing.parseTransactionFile();

        String timeRegex = "[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}Z";
        String patternRegex = timeRegex + ",\"\\s([^\"]*\\s)+\"";
        Pattern pattern = Pattern.compile(patternRegex);
        Matcher matcher = pattern.matcher(rawTransactions);

        while (matcher.find()) {     // find the next match
            String rawTransaction = matcher.group();
            String[] splitTransaction = rawTransaction.split(",\"");
            String transaction = splitTransaction[1].substring(0, splitTransaction[1].length() - 1);
            this.addTransaction(splitTransaction[0], transaction);
        }
        Collections.sort(this.transactions, Transaction.GetComparator());
    }

    private void groupTransactions() {
        for (Object t : this.transactions) {
            Transaction transaction = (Transaction) t;
            Date txnDate = transaction.getDate();
            List currTxn = this.transactionGroups.get(txnDate);
            if (currTxn == null) {
                currTxn = new ArrayList();
            }
            currTxn.add(transaction);
            transactionGroups.put(txnDate, currTxn);
        }
    }

    private void printGroups() {
        for (Date name : transactionGroups.keySet()) {

            String key = name.toString();
            String value = transactionGroups.get(name).toString();
            System.out.println(key + " " + value);
        }
    }

    private void printTransactions() {
        for (Object transaction : this.transactions) {
            Transaction t = (Transaction) transaction;
            System.out.println(t.toString());
        }
    }

    String parseTransactionFile() throws IOException {
        String pathname = "C:\\Users\\tkul\\Documents\\UC Irvine\\Transaction Processing and Distributed Data Management\\Project\\Submission 1\\Databases\\queries\\low_concurrency\\queries.txt";
        byte[] encoded = Files.readAllBytes(Paths.get(pathname));
        return new String(encoded);
    }

    private Timestamp parseTime(String time) {
        Timestamp timestamp = null;

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        try {
            Date parsedDate = dateFormat.parse(time);
            timestamp = new java.sql.Timestamp(parsedDate.getTime());

        } catch (Exception e) {
            System.out.println("failed time parsing");
        }

        return timestamp;
    }

    private void addTransaction(String timestamp, String transactionQuery) {
        Timestamp processedTimestamp = this.parseTime(timestamp);
        Transaction newTransaction = new Transaction(processedTimestamp, transactionQuery);
        this.transactions.add(newTransaction);
    }

    public void postgreSQLCall() {

        System.out.println("\n\n");
            String url = "jdbc:postgresql:tdm_low_concurrency";
            String user = "tushar";
            String password = "tush0906";
            String query = "SELECT sen.name " +
                    "FROM SENSOR sen, SENSOR_TYPE st, COVERAGE_INFRASTRUCTURE ci " +
                    "WHERE sen.SENSOR_TYPE_ID=st.id AND st.name='WiFiAP' AND sen.id=ci.SENSOR_ID AND ci.INFRASTRUCTURE_ID=ANY(array['2038','3231','2019','6066','5211','3044','3066','3216','2204','4226'])";
            try (Connection con = DriverManager.getConnection(url, user, password);
                 Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(query)) {

                if (rs.next()) {
                    System.out.println("SQL Query Result:  " + rs.getString(1));
                }

            } catch (SQLException ex) {
                System.out.println(ex);
            }
        }

//    private final String url = "jdbc:postgresql://localhost/tdm_low_concurrency";
//    private final String user = "tushar";
//    private final String password = "Tgkul95%";

//    public Connection connect() throws SQLException {
//        String url = "jdbc:postgresql://localhost/test";
//        Properties props = new Properties();
//        props.setProperty("user","fred");
//        props.setProperty("password","secret");
//        props.setProperty("ssl","true");
//        Connection conn = DriverManager.getConnection(url, props);
//        String url = "jdbc:postgresql://localhost/test?user=fred&password=secret&ssl=true";
//        Connection conn = DriverManager.getConnection(url);
//        Statement st = conn.createStatement();
//        ResultSet rs = st.executeQuery("SELECT * FROM mytable WHERE columnfoo = 500");
//        while (rs.next()) {
//            System.out.print("Column 1 returned ");
//            System.out.println(rs.getString(1));
//        }
//        rs.close();
//        st.close();
//    }
//
//    public void psqlCall() {
//
//        String SQL = "SELECT sen.name \n" +
//                "FROM SENSOR sen, SENSOR_TYPE st, COVERAGE_INFRASTRUCTURE ci \n" +
//                "WHERE sen.SENSOR_TYPE_ID=st.id AND st.name='WiFiAP' AND sen.id=ci.SENSOR_ID AND ci.INFRASTRUCTURE_ID=ANY(array['2038','3231','2019','6066','5211','3044','3066','3216','2204','4226'])";
//
//        try (Connection conn = connect();
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery(SQL)) {
//            printresults(rs);
//        } catch (SQLException ex) {
//            System.out.println(ex.getMessage());
//        }
//    }
//
//    private void printresults(ResultSet rs) throws SQLException {
//        while (rs.next()) {
//            System.out.println(rs.getString("NAME"));
//        }

}

