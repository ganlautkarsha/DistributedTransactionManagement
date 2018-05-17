import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Operation implements Serializable {
    private static final long serialVersionUID = 1L;
    private Timestamp timeStamp;
    private ArrayList<String> operation;
    private String opr;

    public Operation(Timestamp timeStamp, ArrayList<String> operation) {

        this.timeStamp = timeStamp;
        this.operation = operation;
    }

    public List<String> getOperationList() {
        return this.operation;
    }

    public Operation(Timestamp timeStamp, String operation) {

        this.timeStamp = timeStamp;
        this.opr = operation;
    }

    public String getDate() {

        return this.timeStamp.toString();
    }

    public String getOperation() {
        return opr;
    }


    public static Comparator<Operation> GetComparator() {
        return new Comparator<Operation>() {
            @Override
            public int compare(Operation a, Operation b) {
                return Long.compare(a.timeStamp.getTime(), b.timeStamp.getTime());
            }
        };
    }

}
