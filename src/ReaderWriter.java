import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReaderWriter {

    FileOutputStream fileOut;
    ObjectOutputStream out;
    ObjectInputStream oi;
    ReaderWriter(String role)
    {
        try
        {
        	if(role.equals("writer"))
        	{
        		fileOut = new FileOutputStream("TreeMap_test.txt");
        		out = new ObjectOutputStream(fileOut);
        	}
            
        }
        catch(IOException e)
        {

        }
    }

    @Override
    public void finalize()
    {
        try {
            out.close();
            fileOut.close();
        }
        catch (IOException e)
        {

        }
    }


    public void writeToFile(List<String> operations) throws IOException {
        try {

            for(Object ob:operations)
                out.writeObject(ob);

//            System.out.printf("Serialized data is saved in TreeMap_low_concurrency.ser");
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

//    public void readFromFile(String path) throws IOException, ClassNotFoundException {
//        FileInputStream fi = new FileInputStream(new File(path));
//        ObjectInputStream oi = new ObjectInputStream(fi);
//        List operations = new ArrayList<Operation>();
//        boolean cont = true;
//        while (cont) {
//            try {
//                Object object = oi.readObject();
//                if (object != null) {
//                    operations.add(object);
//                } else {
//                    cont = false;
//                }
//            } catch (Exception e) {
//                cont = false;
//            }
//        }
//
//    }

    public void readFromFile(String path) throws IOException, ClassNotFoundException {

        FileInputStream fi = new FileInputStream(new File(path));
        oi = new ObjectInputStream(fi);

        //        ---- For reading TreeMap.ser ----

//        TreeMap<String,List<String>> allOperations_new=new TreeMap<>();
//        try {
//            FileInputStream fileIn = new FileInputStream("TreeMap_low_concurrency.ser");
//            ObjectInputStream in = new ObjectInputStream(fileIn);
//            allOperations_new = (TreeMap<String, List<String>>) in.readObject();
//            in.close();
//            fileIn.close();
//        } catch (IOException i) {
//            i.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        return  allOperations_new;

    }


    public ArrayList getNext(ArrayList operations) {
        boolean cont = true;
        int count = 0;
        try {
            while (cont && count < 10) {
                Object object = oi.readObject();
                if (object != null) {
                    operations.add(object);
                    count++;
                } else {
                    cont = false;
                }
            }
        } catch (Exception e) {
            cont = false;

        }
        return operations;
    }


//    	boolean cont = true;
//    	int count=0;
//        try {
//        	while(cont)
//        	{
//	            Object object = oi.readObject();
//	            if (object != null) {
//	                operations.add(object);
//	                count++;
//	            }
//	            else
//	            {
//	            	cont=false;
//	            }
//        	}
//        } catch (Exception e) {
//            cont = false;
//
//        }
//        System.out.println("Size=-======"+operations.size());
//        return operations;

//    }
}
