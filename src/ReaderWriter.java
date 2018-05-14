import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.w3c.dom.ls.LSInput;

public class ReaderWriter {
	int BLOCKSIZE=100;
	int check_cnt=0;
    FileOutputStream fileOut;
    ObjectOutputStream out;
    int optimestamp=1;
    ObjectInputStream oi;
    List<List<String>> queue=new ArrayList<>();
    ReaderWriter(String role)
    {
        try
        {
        	if(role.equals("writer"))
        	{
        		
        		fileOut = new FileOutputStream("TreeMap_test.ser");
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
        	System.out.println("Closing file");
            out.close();
            fileOut.close();
        }
        catch (IOException e)
        {

        }
    }

    public void closeFile()
    {
    	try {
        	System.out.println("Closing file");
            out.close();
            fileOut.close();
        }
        catch (IOException e)
        {

        }
    	
    }

    public void writeToFile(ArrayList<String> operations)
    {
        try {

        	out.writeObject("t"+optimestamp);
            for(Object ob:operations)
            {
            	check_cnt++;
                out.writeObject(ob);
            }
            out.flush();
            optimestamp++;

            System.out.println("written-->"+check_cnt);
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

<<<<<<< HEAD
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
=======
    public void readFromFile(String path) throws IOException, ClassNotFoundException {
    	System.out.println(path);
        FileInputStream fi = new FileInputStream(new File(path));
        ObjectInputStream oi = new ObjectInputStream(fi);
        List<String> operations = new ArrayList<String>();
        boolean cont = true;
        while (cont) {
            try {
                Object object = oi.readObject();
//                System.out.println(object.toString());
                if (object != null) {
                    operations.add(object.toString());
                } else {
                    cont = false;
                }
            } catch (Exception e) {
                cont = false;
                e.printStackTrace();
            }
        }
        System.out.println("READ:::::"+operations.size());
        oi.close();
    }

//    public void readFromFile(String path) throws IOException, ClassNotFoundException {
//
//        FileInputStream fi = new FileInputStream(new File(path));
//        oi = new ObjectInputStream(fi);
//        List<String> listop=new ArrayList<String>();
//    	boolean cont = true;
>>>>>>> 12a1ff4659d0709d1d56f5726b3e495b1330819e
//        try {
//        	while(cont)
//        	{
//	            Object opObject = oi.readObject();
//	            if (opObject != null) {
//	            	check_cnt++;
//	            	if(opObject.toString().length()==2)
//	            	{
//	            		if(listop.size()>0)
//		            	{
//		            		queue.add(listop);
//		            		listop.clear();
//		            	}
//	            		
//	            	}
//	            	listop.add(opObject.toString());
//	            }
//	            else
//	            {
//	            	cont=false;
//	            }
//        	}
//        	queue.add(listop);
//        } 
//        catch(EOFException e)
//        {
////        	e.printStackTrace();
//        }	
//        	catch (Exception e) {
//        
//            cont = false;
//            e.printStackTrace();
//        }
//        System.out.println("Size======= "+check_cnt);
//        
//        
//        
//        
//        
//        //        ---- For reading TreeMap.ser ----
//
////        TreeMap<String,List<String>> allOperations_new=new TreeMap<>();
////        try {
////            FileInputStream fileIn = new FileInputStream("TreeMap_low_concurrency.ser");
////            ObjectInputStream in = new ObjectInputStream(fileIn);
////            allOperations_new = (TreeMap<String, List<String>>) in.readObject();
////            in.close();
////            fileIn.close();
////        } catch (IOException i) {
////            i.printStackTrace();
////        } catch (ClassNotFoundException e) {
////            e.printStackTrace();
////        }
////        return  allOperations_new;
//
//    }

    public void triggerInput() throws ClassNotFoundException, IOException
    {
    	Object object = oi.readObject();
        check_cnt++;
        if (object != null) {
        	Operation op=(Operation) object;
//        	threadQueue.add(op.getOperationList());
        	
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
        operations=(ArrayList)TransactionManager.operationMap.remove(0);
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
