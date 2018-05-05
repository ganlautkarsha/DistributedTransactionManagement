import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;

public class Transaction {
    private Timestamp timeStamp;
    private String transaction;
    private StatementPriority statementPriority;

    public Transaction(Timestamp timeStamp, String transaction, StatementPriority statementPriority) {
        this.timeStamp = timeStamp;
        this.transaction = transaction;
        this.statementPriority = statementPriority;
    }

    public Date getTime() {
        return new Date(this.timeStamp.getTime());
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "timeStamp=" + timeStamp +
                ", transaction='" + transaction + '\'' +
                ", statementPriority=" + statementPriority +
                '}';
    }

    public static Comparator<Transaction> GetComparator() {
        return new Comparator<Transaction>() {
            @Override
            public int compare(Transaction a, Transaction b) {
                return Long.compare(a.timeStamp.getTime(), b.timeStamp.getTime());
            }
        };
    }

}
