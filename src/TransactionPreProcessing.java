import java.io.*;
import java.nio.charset.StandardCharsets;
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
    List obeservationOperations;            //contains obervation operations
    Map<String, List<Operation>> operationGroups;   //contains queries as a map (date, operations on that day)

    public TransactionPreProcessing() {
        this.operations = new ArrayList<>();
        this.obeservationOperations = new ArrayList<>();
        this.operationGroups = new HashMap<String, List<Operation>>();
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        TransactionPreProcessing transactionPreProcessing = new TransactionPreProcessing();
        transactionPreProcessing.processQueries();
        transactionPreProcessing.parseObservationOperationFile();
    }

    private void processQueries() throws IOException {
        this.generateQueryOperations();
        this.groupTransactions();
        //writing to db logic here
    }


    //use same for semantic file
    private void parseObservationOperationFile() throws IOException {
        String pathName = "./src/data/low_concurrency/observation_low_concurrency.sql";
        int count = 0;

        try (BufferedReader br = Files.newBufferedReader(Paths.get(pathName), StandardCharsets.UTF_8)) {
            for (String line; (line = br.readLine()) != null; ) {

                if (line.contains("INSERT")) {
                    this.parseObservations(line);
                    count++;
                    if (count > 1000000) { //for blocks
                        count = 0;
                        //writing to db logic here
                        this.obeservationOperations = new ArrayList<>();
                    }
                } else {
                    continue;
                }

            }

        }
    }


    private String parseObservations(String rawObservation) {
        String dateRegex = "[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}";
        Pattern timePattern = Pattern.compile(dateRegex);
        Matcher matcher = timePattern.matcher(rawObservation);
        matcher.find();
        String date = matcher.group();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.US);
        Operation newOperation = parseToOperation(date, rawObservation, this.obeservationOperations, dateFormat);
        this.obeservationOperations.add(newOperation);
        return newOperation.getDate();
    }

    //for queries
    private void generateQueryOperations() throws IOException {
        String rawTransactions = this.parseQueryOperationFile();

        String timeRegex = "[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}Z";
        String patternRegex = timeRegex + ",\"\\s([^\"]*\\s)+\"";
        Pattern pattern = Pattern.compile(patternRegex);
        Matcher matcher = pattern.matcher(rawTransactions);
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);

        while (matcher.find()) {     // find the next match
            String rawTransaction = matcher.group();
            String[] splitTransaction = rawTransaction.split(",\"");
            String transaction = splitTransaction[1].substring(0, splitTransaction[1].length() - 1);
            Operation newOperation = this.parseToOperation(splitTransaction[0], transaction, this.operations, dateFormat);
            this.operations.add(newOperation);

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


    private String parseQueryOperationFile() throws IOException {
        String pathname = "./src/queries/low_concurrency/queries.txt";
        byte[] encoded = Files.readAllBytes(Paths.get(pathname));
        return new String(encoded);
    }

    private Timestamp parseTime(String time, SimpleDateFormat dateFormat) {
        Timestamp timestamp = null;

        try {
            Date parsedDate = dateFormat.parse(time);
            timestamp = new java.sql.Timestamp(parsedDate.getTime());

        } catch (Exception e) {
            System.out.println("failed time parsing");
        }

        return timestamp;
    }

    private Operation parseToOperation(String timestamp, String transactionQuery, List operations, SimpleDateFormat dateFormat) {
        Timestamp processedTimestamp = this.parseTime(timestamp, dateFormat);
        Operation newOperation = new Operation(processedTimestamp, transactionQuery, StatementPriority.HIGH);
        return newOperation;
    }


}

