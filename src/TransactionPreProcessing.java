import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
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
        Transaction newTransaction = new Transaction(processedTimestamp, transactionQuery);
        this.transactions.add(newTransaction);
    }


}
