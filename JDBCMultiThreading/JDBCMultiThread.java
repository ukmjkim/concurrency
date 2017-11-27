/* How to run the program */
/* export CLASSPATH=.:/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/lib/mysql-connector-java-5.1.44-bin.jar; java JDBCMultiThread */

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.LinkedList;

public class JDBCMultiThread extends Thread {
  private static int NUM_OF_THREADS = 10;
  private final static String CONNECT_STRING = "jdbc:mysql://serpstat:3306/serpstat?autoReconnect=true&useSSL=false";
  private final static String USER = "serpstat";
  private final static String PWD = "statserp";

  int m_myId;

  static int c_nextId = 1;

  synchronized static int getNextId() {
    return c_nextId++;
  }

  public static void main(String[] args) {
    try {
      // The newInstance() call is a work around for some
      // broken Java implementations
      Class.forName("com.mysql.jdbc.Driver").newInstance();
    } catch (Exception e) {
      System.out.println("Where is your MySQL JDBC Driver?");
      e.printStackTrace();
      System.exit(0);
    }

    if (args.length > 1) {
      System.out.println("Error: Invalid Syntax.");
      System.out.println("java JDBCMultiThread [NoOfThreads]");
      System.exit(0);
    } else if (args.length == 1) {
      NUM_OF_THREADS = Integer.parseInt(args[0]);
    }

    long start = System.currentTimeMillis();

    // Create the threads
    Thread[] threadList = new Thread[NUM_OF_THREADS];

    try {
      // Spawn threads
      for (int i = 0; i < NUM_OF_THREADS; i++) {
        threadList[i] = new JDBCMultiThread();
        threadList[i].start();
      }

      // Wait for all threads to end
      for (int i = 0; i < NUM_OF_THREADS; i++) {
        threadList[i].join();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    long end = System.currentTimeMillis();
    System.out.println("Execution Time: " + (end - start));
  }

  public JDBCMultiThread() {
    super();
    m_myId = getNextId();
  }

  public void run() {
    Connection conn = null;
    ResultSet rs = null;
    Statement stmt = null;

    try {
      conn = DriverManager.getConnection(CONNECT_STRING, USER, PWD);
      stmt = conn.createStatement();
      rs = stmt.executeQuery("SELECT * FROM KEYWORDS");
      while (rs.next())
        System.out.println("Thread " + m_myId 
                          + " Keyword Id: " + rs.getLong(1)
                          + " Keyword: " + rs.getString(3));
      rs.close();
      stmt.close();
      if (conn != null) conn.close();

      System.out.println("Thread " + m_myId +  " is finished. ");
    } catch (Exception e) {
      System.out.println("Thread " + m_myId + " got Exception: " + e);
      e.printStackTrace();
      return;
    }
  }
}
