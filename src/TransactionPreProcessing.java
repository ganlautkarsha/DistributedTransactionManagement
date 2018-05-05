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

}

