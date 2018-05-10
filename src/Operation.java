import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Comparator;

public class Operation implements Serializable {
    private static final long serialVersionUID = 1L;
    private Timestamp timeStamp;
    private String operation;
    private StatementPriority operationPriority;

    public Operation(Timestamp timeStamp, String operation, StatementPriority operationPriority) {

        this.timeStamp = timeStamp;
        this.operation = operation;
        this.operationPriority = operationPriority;
    }

    public String getDate() {
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        Date date = new Date(this.timeStamp.getTime());
//        return dateFormat.format(date);
        return this.timeStamp.toString();
    }

    public String getOperation()
    {
        return operation;
    }

//    @Override
//    public String toString() {
//        return "Operation{" +
//                "timeStamp=" + timeStamp +
//                ", operation='" + operation + '\'' +
//                ", operationPriority=" + operationPriority +
//                '}';
//    }

    public static Comparator<Operation> GetComparator() {
        return new Comparator<Operation>() {
            @Override
            public int compare(Operation a, Operation b) {
                return Long.compare(a.timeStamp.getTime(), b.timeStamp.getTime());
            }
        };
    }

}
