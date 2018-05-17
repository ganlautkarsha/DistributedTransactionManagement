import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class ReaderWriter {
    FileOutputStream fileOut;
    ObjectOutputStream out;

    ReaderWriter(String role) {
        try {
            if (role.equals("writer")) {
                fileOut = new FileOutputStream("TreeMap_test.txt");
                out = new ObjectOutputStream(fileOut);
            }

        } catch (IOException e) {

        }
    }

    @Override
    public void finalize() {
        try {
            out.close();
            fileOut.close();
        } catch (IOException e) {

        }
    }


    public TreeMap<String, List<String>> readFromFile(String path) throws IOException, ClassNotFoundException {

        TreeMap<String, List<String>> allOperations_new = new TreeMap<>();
        try {
            FileInputStream fileIn = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            allOperations_new = (TreeMap<String, List<String>>) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return allOperations_new;

    }


}
