import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class ReaderWriter {
	int BLOCKSIZE=100;
	int check_cnt=0;
    FileOutputStream fileOut;
    ObjectOutputStream out;
    ObjectInputStream oi;
    List<String> queue=new ArrayList<>();
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


    public void writeToFile(List<Operation> operations)
    {
        try {

        	int c=0;
        	out.writeObject(operations);
//            for(Object ob:operations)
//            {
//            	c++;
//                out.writeObject(ob);
//            }

            System.out.println("written-->"+c);
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

    public TreeMap<String, List<String>> readFromFile(String path) throws IOException, ClassNotFoundException {

        TreeMap<String,List<String>> allOperations_new=new TreeMap<>();
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
        return  allOperations_new;
        
        
        
        
        
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

    public void triggerInput() throws ClassNotFoundException, IOException
    {
    	Object object = oi.readObject();
        check_cnt++;
        if (object != null) {
        	Operation op=(Operation) object;
//        	queue.add(op.getOperationList());
        	
//            operations.add(object);
//            count++;
        } else {
//            break;
        }
    }

    public ArrayList<String> getNext(ArrayList<String> operations) {
    	
    	
//    	operations.clear();
//        int count = 0;
//        try {
//            while (count < BLOCKSIZE) {
//                Object object = oi.readObject();
//                check_cnt++;
//                if (object != null) {
//                    operations.add(object);
//                    count++;
//                } else {
//                    break;
//                }
//            }
//        } catch (Exception e) {
//
//        }
        System.out.println("Current check count="+check_cnt+"returning size="+operations.size());
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
//
//    }
}
