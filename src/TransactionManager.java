import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.invoke.SerializedLambda;
import java.sql.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

class Statistics
{
	public static int counter;
	public static ArrayList<Integer> MPL_list=new ArrayList<>();
	public static ArrayList<Float> throughput=new ArrayList<>();
	
}

class Demo extends TimerTask {
    public void run() {
          System.out.println("Hello World"); 
     }
}

class TransactionExecuter extends Thread
{
	final Object lock = new Object(); // globally visible lock object
	ArrayList<String> listoperations;
	public TransactionExecuter(ArrayList<String> listoperations) {
		// TODO Auto-generated constructor stub
		this.listoperations=new ArrayList<>(listoperations);
	}
	public void run()
	{
		
		System.out.println("**********Thread ID: "+this.getId());
		postgreSQLCall(listoperations);
		synchronized(lock)
		{
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
            	
                for(String op: listoperations)
                {
                	String operation=op.replace("\n", "").trim();
//                	System.out.println("operation= "+operation);
                	if(operation.startsWith("SELECT"))
                	{
                		ResultSet rs = st.executeQuery(operation);
                		if (rs.next()) {
//                            System.out.println("SQL Query Result:  " + rs.getString(1));
                        }
                	}
                	else
                	{
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


public class TransactionManager {
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
			readerObj.readFromFile("TreeMap_test.txt");
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

			while (Thread.activeCount() > 1) {
			}
				Timestamp end_timestamp = new Timestamp(System.currentTimeMillis());
				long milliseconds = end_timestamp.getTime() - start_timestamp.getTime();
			    int seconds = (int) milliseconds / 1000;
			    System.out.println("Number of transactions executed="+Statistics.counter+" time="+seconds+"for MPL="+MAX_THREADS);
				System.out.println("Final exit!");
				Statistics.MPL_list.add(MAX_THREADS);
				Statistics.throughput.add((float)Statistics.counter/seconds);
			
			
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch  block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	
	}
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TransactionManager manager=new TransactionManager();
		for(int i=10;i<=100;i=i+10)
			{
				manager.readTransactions(i);
				break;
			}
		
//		for(int i=0;i<Statistics.MPL_list.size();i++)
//		{
//			System.out.println("MPL :"+Statistics.MPL_list.get(i)+" Throughput : "+Statistics.throughput.get(i));
//		}
//		
//		Timer t1 = new Timer();
//		t1.schedule(new Demo(), 0,600);
			
	}

}
