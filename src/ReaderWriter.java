import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReaderWriter {
    public void writeToFile(List operations, String prefix, String date) throws IOException {
        File file = new File(prefix + date + ".txt");
        if (file.exists()) {
            System.out.println("I exist!");
        }
        FileOutputStream f = new FileOutputStream(file);

        ObjectOutputStream o = new ObjectOutputStream(f);
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
