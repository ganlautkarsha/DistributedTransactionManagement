import java.io.IOException;
import java.sql.*;
import java.util.*;

class Statistics {
    public static int counter;
    public static ArrayList<Integer> MPL_list = new ArrayList<>();
    public static ArrayList<Float> throughput = new ArrayList<>();

}


class TransactionExecuter extends Thread {
    final Object lock = new Object(); // globally visible lock object
    ArrayList<String> listoperations;

    public TransactionExecuter(ArrayList<String> listoperations) {
        // TODO Auto-generated constructor stub
        this.listoperations = new ArrayList<>(listoperations);
    }

    public void run() {

        System.out.println("**********Thread ID: " + this.getId());
        postgreSQLCall(listoperations);
        synchronized (lock) {
            Statistics.counter++;
        }
    }

    public void postgreSQLCall(ArrayList<String> listoperations) {

        System.out.println("\n\n");
        String url = "jdbc:postgresql://localhost/tdm_low_concurrency";
        String user = "postgres";
        String password = "password";

        try {
            Connection con = DriverManager.getConnection(url, user, password);
            Statement st = con.createStatement();
            con.setAutoCommit(false);

            //iterate through the list of operations and execute

            for (String op : listoperations) {
                String operation = op.replace("\n", "").trim();
//                	System.out.println("operation= "+operation);
                if (operation.startsWith("SELECT")) {
                    ResultSet rs = st.executeQuery(operation);
                    if (rs.next()) {
//                            System.out.println("SQL Query Result:  " + rs.getString(1));
                    }
                } else {
                    st.executeUpdate(operation);
                }
            }
            con.commit();
            con.close();
            st.close();
            System.out.println("Closing connection");

        } catch (SQLException ex) {
            System.out.println(ex);
        }

    }
}

class Demo extends TimerTask {
    int i=0;
    public void run() {
        System.out.println("Hello World"+i);
        System.out.println("Adding to queue");
        i++;
        TransactionManager.threadQueue.add(TransactionManager.operationMap.get(i));

    }
}
public class TransactionManager {
<<<<<<< HEAD
    ReaderWriter readerObj;
    public static List<List<String>> threadQueue = new ArrayList<>();
    int MAX_THREADS = 50;
    public static Map<Integer, List<String>> operationMap;



    public TransactionManager() {
        // TODO Auto-generated constructor stub
        readerObj = new ReaderWriter("reader");
        operationMap = new HashMap<Integer, List<String>>();
    }


    public ArrayList<String> getNext() {
        ArrayList<String> operations = new ArrayList<>();
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
        System.out.println("In getNext");
        if(threadQueue.size() != 0) {
//            System.out.println("Adding threadQueue element: " + threadQueue.get(0));
            operations = (ArrayList<String>) threadQueue.remove(0);
        }
        return operations;
    }


    public void readTransactions() throws IOException, ClassNotFoundException {
        TreeMap<String, List<String>> allOperations_new = new TreeMap<>();
        allOperations_new = readerObj.readFromFile("TreeMap_low_concurrency.ser");
        int count = 0;
        for (Map.Entry<String, List<String>> opEntry : allOperations_new.entrySet()) {
            count += opEntry.getValue().size();
        }
//        System.out.println(count);

        Timestamp prev = null;
        boolean first=true;
        int totalOperationsCount = 0;
        ArrayList<String> opGroup = new ArrayList<>();
        int total = 0;
        int batchCount =0;
        for (Map.Entry<String, List<String>> opEntry : allOperations_new.entrySet()) {
            String opKey = opEntry.getKey();
            totalOperationsCount += opEntry.getValue().size();
            Timestamp current = Timestamp.valueOf(opKey);

            if (first)
            {
                prev=Timestamp.valueOf(opKey);
                opGroup.addAll(opEntry.getValue());
                first=false;
            }
            else{
                if(current.getTime() - prev.getTime() <= 179999)
                {
                    opGroup.addAll(opEntry.getValue());
                }
                else {
                    this.operationMap.put(batchCount,opGroup);
                    opGroup = new ArrayList<>();
                    opGroup.addAll(opEntry.getValue());
                    batchCount++;
                    prev = current;
                }
            }
        }
        count = 0;
        for(Map.Entry<Integer, List<String>> opEntry : this.operationMap.entrySet()) {
            count += opEntry.getValue().size();
        }
        System.out.println(count);
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // TODO Auto-generated method stub
        TransactionManager manager = new TransactionManager();

//        Timer t1 = new Timer();
//        t1.schedule(new Demo(), 0,600);
=======
	ReaderWriter readerObj;
	int MAX_THREADS=50;
	
	public TransactionManager() {
		// TODO Auto-generated constructor stub
		readerObj=new ReaderWriter("reader");
	}
	
	
	public void readTransactions(int max)
	{
		this.MAX_THREADS=max;
		try {
			int threadcount=0;
			ArrayList<ArrayList<String>> queue=new ArrayList<>();
			ArrayList<String> listoperations=new ArrayList<>();
			readerObj.readFromFile("TreeMap_test.ser");
			int count=0;
			Timestamp start_timestamp = new Timestamp(System.currentTimeMillis());
			boolean flag=true;
			int nothread=0;
			while( queue.size()>0 || readerObj.getNext(listoperations).size()!=0)
			{
				if(listoperations.size()!=0)
					queue.add(listoperations);
				System.out.println("Pending sets="+queue.size());
				System.out.println("***COUNT=: "+Thread.activeCount());
				while(Thread.activeCount()>=MAX_THREADS)
				{
					if(flag && readerObj.getNext(listoperations).size()>0)
						queue.add(listoperations);
					else
						flag=false;
				}
				if(Thread.activeCount()<MAX_THREADS && queue.size()>0)
				{
					count+=queue.get(0).size();
					System.out.println("Threads created====>"+(++nothread));
					System.out.println("Assigning work of size"+queue.get(0).size());
					System.out.println("Executing :::::"+count);
					TransactionExecuter transaction=new TransactionExecuter(queue.remove(0));
					transaction.start();
					threadcount++;
				}
				listoperations.clear();
	        	
			}
>>>>>>> 12a1ff4659d0709d1d56f5726b3e495b1330819e

		for(int i=10;i<=100;i=i+10)
			{
        manager.readTransactions();
				break;
			}

//		for(int i=0;i<Statistics.MPL_list.size();i++)
//		{
//			System.out.println("MPL :"+Statistics.MPL_list.get(i)+" Throughput : "+Statistics.throughput.get(i));
//		}



    }

}
