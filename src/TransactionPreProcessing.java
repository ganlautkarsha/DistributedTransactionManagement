import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TransactionPreProcessing {
    List operations;
    Map<String, List<Operation>> operationGroups;
    ReaderWriter readerWriter;

    public TransactionPreProcessing() {
        this.operations = new ArrayList<>();
        this.operationGroups = new HashMap<String, List<Operation>>();
        this.readerWriter = new ReaderWriter();
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        TransactionPreProcessing transactionPreProcessing = new TransactionPreProcessing();
        transactionPreProcessing.generateTransactions(transactionPreProcessing);
        transactionPreProcessing.groupTransactions();
        transactionPreProcessing.writeToFiles(transactionPreProcessing, "./src/preprocessedFiles/Queries");
    }

    private void generateTransactions(TransactionPreProcessing transactionPreProcessing) throws IOException {
        String rawTransactions = transactionPreProcessing.parseOperationFile();

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
        Collections.sort(this.operations, Operation.GetComparator());
    }


    private void groupTransactions() {
        for (Object t : this.operations) {
            Operation operation = (Operation) t;
            String txnDate = operation.getDate();
            List currTxn = this.operationGroups.get(txnDate);
            if (currTxn == null) {
                currTxn = new ArrayList();
            }
            currTxn.add(operation);
            operationGroups.put(txnDate, currTxn);
        }
    }


    private void writeToFiles(TransactionPreProcessing transactionPreProcessing, String prefix) throws IOException {

        for (Object d : transactionPreProcessing.operationGroups.keySet()) {
            String date = (String) d;
            this.readerWriter.writeToFile(transactionPreProcessing.operationGroups, prefix, date);

        }
    }


    private String parseOperationFile() throws IOException {
        String pathname = "./src/queries/low_concurrency/queries.txt";
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
        Operation newOperation = new Operation(processedTimestamp, transactionQuery, StatementPriority.HIGH);
        this.operations.add(newOperation);
    }

    private void printGroups() {
        for (String name : operationGroups.keySet()) {
            String key = name.toString();
            String value = operationGroups.get(name).toString();
            System.out.println(key + " " + value);
        }
    }

    private void printTransactions() {
        for (Object transaction : this.operations) {
            Operation t = (Operation) transaction;
            System.out.println(t.toString());
        }
    }

//    public void postgreSQLCall() {
//
//        System.out.println("\n\n");
//        String url = "jdbc:postgresql:tdm_low_concurrency";
//        String user = "tushar";
//        String password = "tush0906";
//        String query = "SELECT sen.name " +
//                "FROM SENSOR sen, SENSOR_TYPE st, COVERAGE_INFRASTRUCTURE ci " +
//                "WHERE sen.SENSOR_TYPE_ID=st.id AND st.name='WiFiAP' AND sen.id=ci.SENSOR_ID AND ci.INFRASTRUCTURE_ID=ANY(array['2038','3231','2019','6066','5211','3044','3066','3216','2204','4226'])";
//        try (Connection con = DriverManager.getConnection(url, user, password);
//             Operation st = con.createStatement();
//             ResultSet rs = st.executeQuery(query)) {
//
//            if (rs.next()) {
//                System.out.println("SQL Query Result:  " + rs.getString(1));
//            }
//
//        } catch (SQLException ex) {
//            System.out.println(ex);
//        }
//    }

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
//        Operation st = conn.createStatement();
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
//             Operation stmt = conn.createStatement();
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

