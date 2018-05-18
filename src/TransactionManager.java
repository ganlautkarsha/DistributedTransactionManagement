import java.io.IOException;
import java.sql.*;
import java.util.*;


public class TransactionManager {
    ReaderWriter readerObj;
    public static List<List<String>> threadQueue = new ArrayList<>();
    public static Map<Integer, List<String>> operationMap;


    public TransactionManager() {
        // TODO Auto-generated constructor stub
        readerObj = new ReaderWriter("reader");
        operationMap = new HashMap<Integer, List<String>>();
    }


    public ArrayList<String> getNext() {
        ArrayList<String> operations = new ArrayList<>();

        System.out.println("In getNext");
        if (threadQueue.size() != 0) {
            operations = (ArrayList<String>) threadQueue.remove(0);
        }
        return operations;
    }


    public void readTransactions() throws IOException, ClassNotFoundException {
        TreeMap<String, List<String>> allOperations_new = new TreeMap<>();
        allOperations_new = readerObj.readFromFile("TreeMap_low_concurrency.ser");
        int count = 0;
        for (Map.Entry<String, List<String>> opEntry : allOperations_new.entrySet()) {
            count += opEntry.getValue().size();
        }

        Timestamp prev = null;
        boolean first = true;
        int totalOperationsCount = 0;
        ArrayList<String> opGroup = new ArrayList<>();
        int batchCount = 0;
        for (Map.Entry<String, List<String>> opEntry : allOperations_new.entrySet()) {
            String opKey = opEntry.getKey();
            totalOperationsCount += opEntry.getValue().size();
            Timestamp current = Timestamp.valueOf(opKey);

            if (first) {
                prev = Timestamp.valueOf(opKey);
                opGroup.addAll(opEntry.getValue());
                first = false;
            } else {
                if (current.getTime() - prev.getTime() <= 179999) {
                    opGroup.addAll(opEntry.getValue());
                } else {
                    this.operationMap.put(batchCount, opGroup);
                    opGroup = new ArrayList<>();
                    opGroup.addAll(opEntry.getValue());
                    batchCount++;
                    prev = current;
                }
            }
        }
        count = 0;
        for (Map.Entry<Integer, List<String>> opEntry : this.operationMap.entrySet()) {
            count += opEntry.getValue().size();
        }
        System.out.println(count);
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // TODO Auto-generated method stub
        TransactionManager manager = new TransactionManager();


        for (int i = 10; i <= 100; i = i + 10) {
            manager.readTransactions();
            break;
        }


    }

}
