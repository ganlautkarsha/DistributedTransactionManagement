import java.sql.Timestamp;
import java.util.Comparator;

public class Transaction {
    private Timestamp timeStamp;
    private String transaction;

    public Transaction(Timestamp timeStamp, String transaction) {
        this.timeStamp = timeStamp;
        this.transaction = transaction;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "timeStamp='" + timeStamp + '\'' +
                ", transaction='" + transaction + '\'' +
                '}';
    }


    public static Comparator<Transaction> TransactionComparator
            = new Comparator<Transaction>() {
        @Override
        public int compare(Transaction a, Transaction b) {
            return Long.compare(a.timeStamp.getTime(), b.timeStamp.getTime());
        }
    };
}
