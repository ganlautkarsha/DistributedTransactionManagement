import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReaderWriter {
    public void writeToFile(Map operationGroup, String prefix, String date) throws IOException {
        FileOutputStream f = new FileOutputStream(new File(prefix + date + ".txt"));
        ObjectOutputStream o = new ObjectOutputStream(f);
        List operations = (List) operationGroup.get(date);
        for (Object ob : operations) {
            Operation op = (Operation) ob;
            o.writeObject(op);
        }
        o.close();
        f.close();
    }

    public void readFromFile(String path) throws IOException, ClassNotFoundException {
        FileInputStream fi = new FileInputStream(new File(path));
        ObjectInputStream oi = new ObjectInputStream(fi);
        List operations = new ArrayList<Operation>();
        boolean cont = true;
        while (cont) {
            try {
                Object object = oi.readObject();
                if (object != null) {
                    operations.add(object);
                } else {
                    cont = false;
                }
            } catch (Exception e) {
                cont = false;
            }
        }

    }
}
